package com.example.kinodata.fragments.profile.lists.watchlist.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.databinding.FragmentMoviesWatchlistBinding
import com.example.kinodata.fragments.profile.lists.watchlist.WatchlistFragmentDirections
import com.example.kinodata.fragments.profile.lists.watchlist.WatchlistViewModel
import com.example.kinodata.fragments.profile.lists.watchlist.adapter.MoviesWatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesWatchlistFragment : Fragment() {
    private var _binding: FragmentMoviesWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesWatchlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = MoviesWatchlistAdapter()
        binding.rvMoviesWatchlist.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getMoviesWatchlist()
        viewModel.movies.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collect {
                    mAdapter.submitData(it)
                }
            }

        }

        mAdapter.onItemClick = { movie ->
            movie?.id?.let { id ->
                val action = WatchlistFragmentDirections
                    .actionWatchlistFragmentToMovieDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}