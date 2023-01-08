package com.example.kinodata.fragments.movies.movieDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.adapters.CrewVerticalAdapter
import com.example.kinodata.databinding.FragmentAllMovieCrewBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMovieCrewFragment : Fragment() {

    val viewModel: MovieDetailsViewModel by viewModels()

    private var _binding: FragmentAllMovieCrewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMovieCrewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllMovieCrew.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = CrewVerticalAdapter()
        binding.rvAllMovieCrew.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }
        viewModel.getMovieCredits()
        viewModel.credits.observe(viewLifecycleOwner) {
            val sortedList = it.crew.sortedByDescending { it.popularity }
            adapter.updateData(sortedList)
        }

        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllMovieCrewFragmentDirections
                    .actionAllMovieCrewFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}