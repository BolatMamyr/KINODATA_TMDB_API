package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.R
import com.example.kinodata.adapters.PersonActingMoviesVerticalAdapter
import com.example.kinodata.adapters.PersonActingTvVerticalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentPersonAllFilmographyBinding
import com.example.kinodata.fragments.people.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAllFilmographyFragment : Fragment() {

    private var _binding: FragmentPersonAllFilmographyBinding? = null
    private val binding get() = _binding!!

    private val args: PersonAllFilmographyFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView

    private val viewModel: PersonViewModel by viewModels()

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

        recyclerView = binding.rvPersonAllMovies
        recyclerView.apply {
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            isSaveEnabled = true
            layoutManager = manager
            setHasFixedSize(true)
        }
        if (args.category == MyConstants.CATEGORY_ACTING_MOVIES) {
            binding.tbPersonAllMovies.title = getString(R.string.filmography_movies)
            val adapter = PersonActingMoviesVerticalAdapter()
            recyclerView.adapter = adapter
            viewModel.getPersonMovieCredits()
            viewModel.actingMovies.observe(viewLifecycleOwner) {
                val sortedList = it.sortedByDescending { it.release_date }
                adapter.updateData(sortedList)
            }

            adapter.onItemClick = {
                it?.id?.let { movieId ->
                    val action = PersonAllFilmographyFragmentDirections
                        .actionPersonAllMoviesFragmentToMovieDetailsFragment(movieId)
                    findNavController().navigate(action)
                }
            }
        } else if (args.category == MyConstants.CATEGORY_ACTING_TV) {
            binding.tbPersonAllMovies.title = getString(R.string.filmography_tvSeries)
            val adapter = PersonActingTvVerticalAdapter()
            recyclerView.adapter = adapter
            viewModel.getPersonTvSeriesCredits()
            viewModel.actingTv.observe(viewLifecycleOwner) {
                val sortedList = it.sortedByDescending { it.first_air_date }
                adapter.updateData(sortedList)
            }
            adapter.onItemClick = {
                it?.id?.let { tvId ->
                    val action = PersonAllFilmographyFragmentDirections
                        .actionPersonAllMoviesFragmentToTvDetailsFragment(tvId)
                    findNavController().navigate(action)
                }
            }
        } else if (args.category == MyConstants.CATEGORY_MOVIES_AS_CREW) {
            // TODO: FINISH THIS BLOCK
        } else {

        }





    }

}