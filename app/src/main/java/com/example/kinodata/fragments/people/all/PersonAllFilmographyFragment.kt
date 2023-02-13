package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.R
import com.example.kinodata.fragments.people.all.adaptersVertical.PersonActingMoviesVerticalAdapter
import com.example.kinodata.fragments.people.all.adaptersVertical.PersonActingTvVerticalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentPersonAllFilmographyBinding
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAllFilmographyFragment : Fragment() {

    private var _binding: FragmentPersonAllFilmographyBinding? = null
    private val binding get() = _binding!!

    private val args: PersonAllFilmographyFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView

    private val viewModel: PersonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonAllFilmographyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbPersonAllMovies.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (args.personId != viewModel.personId.value) {
            viewModel.getPersonInfo(args.personId)
            viewModel.getPersonMovieCredits(args.personId)
            viewModel.getPersonTvSeriesCredits(args.personId)
            viewModel.setPersonId(args.personId)
        }
        recyclerView = binding.rvPersonAllMovies
        recyclerView.apply {
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            isSaveEnabled = true
            layoutManager = manager
            setHasFixedSize(true)
        }
        when (args.category) {
            MyConstants.CATEGORY_ACTING_MOVIES -> {
                binding.tbPersonAllMovies.title = getString(R.string.filmography_movies)
                val adapter = PersonActingMoviesVerticalAdapter()
                recyclerView.adapter = adapter
                viewModel.movies.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            val sortedList = it.data.cast.sortedByDescending { it.release_date }
                            adapter.updateData(sortedList)
                        }
                        else -> {

                        }
                    }
                }

                adapter.onItemClick = {
                    it?.id?.let { movieId ->
                        val action = PersonAllFilmographyFragmentDirections
                            .actionPersonAllMoviesFragmentToMovieDetailsFragment(movieId)
                        findNavController().navigate(action)
                    }
                }
            }
            MyConstants.CATEGORY_ACTING_TV -> {
                binding.tbPersonAllMovies.title = getString(R.string.filmography_tvSeries)
                val adapter = PersonActingTvVerticalAdapter()
                recyclerView.adapter = adapter
                viewModel.tv.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            val sortedList = it.data.cast.sortedByDescending { it.first_air_date }
                            adapter.updateData(sortedList)
                        }
                        else -> {

                        }
                    }
                }
                adapter.onItemClick = {
                    it?.id?.let { tvId ->
                        val action = PersonAllFilmographyFragmentDirections
                            .actionPersonAllMoviesFragmentToTvDetailsFragment(tvId)
                        findNavController().navigate(action)
                    }
                }
            }
            else -> {

            }
        }





    }

}