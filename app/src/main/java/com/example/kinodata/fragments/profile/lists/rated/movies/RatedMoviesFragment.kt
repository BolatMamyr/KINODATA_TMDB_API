package com.example.kinodata.fragments.profile.lists.rated.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentFavoriteMoviesBinding
import com.example.kinodata.databinding.FragmentRatedMoviesBinding
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteListFragmentDirections
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoriteMoviesAdapter
import com.example.kinodata.fragments.profile.lists.rated.RatedListFragmentDirections
import com.example.kinodata.fragments.profile.lists.rated.RatedViewModel
import com.example.kinodata.fragments.profile.lists.rated.adapter.RatedMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RatedMoviesFragment"

@AndroidEntryPoint
class RatedMoviesFragment : Fragment() {

    private var _binding: FragmentRatedMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RatedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatedMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        val mAdapter = RatedMoviesAdapter()
        binding.rvRatedMovies.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
        }
        viewModel.getRatedMovies()
        viewModel.ratedMovies.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collectLatest {
                    mAdapter.submitData(it)
                }
            }
        }

        mAdapter.onItemClick = { movie ->
            movie?.id?.let { id ->
                val action = RatedListFragmentDirections
                    .actionRatedListFragmentToMovieDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}