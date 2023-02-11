package com.example.kinodata.fragments.movies.movieDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.adapters.CrewVerticalAdapter
import com.example.kinodata.databinding.FragmentAllMovieCrewBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMovieCrewFragment : Fragment() {

    val viewModel: MovieDetailsViewModel by activityViewModels()

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

        viewModel.credits.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    adapter.updateData(it.data.crew)
                }
                is NetworkResult.Error -> {
                    val message = it.throwable.message ?: getString(R.string.something_went_wrong)
                    toast(message)
                }
                else -> {
                    // TODO: add progressbar
                }
            }
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