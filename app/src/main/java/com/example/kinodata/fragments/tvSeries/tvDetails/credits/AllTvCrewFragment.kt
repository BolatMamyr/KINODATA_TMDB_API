package com.example.kinodata.fragments.tvSeries.tvDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.adapters.CrewVerticalAdapter
import com.example.kinodata.databinding.FragmentAllMovieCrewBinding
import com.example.kinodata.databinding.FragmentAllTvCrewBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModelFactory
import com.example.kinodata.fragments.movies.movieDetails.credits.AllMovieCrewFragmentArgs
import com.example.kinodata.fragments.movies.movieDetails.credits.AllMovieCrewFragmentDirections
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModelFactory
import com.example.kinodata.repo.Repository

class AllTvCrewFragment : Fragment() {

    private val args: AllTvCrewFragmentArgs by navArgs()

    val viewModel: TvDetailsViewModel by viewModels {
        TvDetailsViewModelFactory(Repository(), args.tvId.toString())
    }

    private var _binding: FragmentAllTvCrewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTvCrewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllTvCrew.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = CrewVerticalAdapter()
        binding.rvAllTvCrew.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }

        viewModel.getTvCredits()
        viewModel.credits.observe(viewLifecycleOwner) {
            val sortedList = it.crew.sortedByDescending { it.popularity }
            adapter.updateData(sortedList)
        }
        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllTvCrewFragmentDirections
                    .actionAllTvCrewFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}