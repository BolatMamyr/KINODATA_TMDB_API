package com.example.kinodata.fragments.profile.lists.favorite.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.databinding.FragmentFavoriteMoviesBinding
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteListFragmentDirections
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteViewModel
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoriteMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "FavoriteMoviesFragment"

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = FavoriteMoviesAdapter()
        binding.rvFavoriteMovies.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getFavoriteMovies()
        viewModel.favoriteMovies.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collect {
                    mAdapter.submitData(it)
                }
            }

        }

        mAdapter.onItemClick = { movie ->
            movie?.id?.let { id ->
                val action = FavoriteListFragmentDirections
                    .actionFavoriteListFragmentToMovieDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}