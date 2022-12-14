package com.example.kinodata.fragments.movies.movieDetails.cast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.adapters.CastVerticalAdapter
import com.example.kinodata.databinding.FragmentAllMovieCastBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModelFactory
import com.example.kinodata.repo.Repository

class AllMovieCastFragment : Fragment() {
    private val args: AllMovieCastFragmentArgs by navArgs()

    val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(Repository(), args.movieId.toString())
    }

    private var _binding: FragmentAllMovieCastBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMovieCastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllMovieCast.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val adapter = CastVerticalAdapter()
        binding.rvAllMovieCast.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }
        viewModel.getMovieCredits()
        viewModel.credits.observe(viewLifecycleOwner) {
            val cast = it.cast
            adapter.updateData(cast)
        }

        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllMovieCastFragmentDirections
                    .actionAllMovieCastFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}