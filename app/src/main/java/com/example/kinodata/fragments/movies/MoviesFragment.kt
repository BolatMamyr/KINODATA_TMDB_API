package com.example.kinodata.fragments.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentMoviesBinding
import com.example.kinodata.fragments.movies.adapters.MoviesHorizontalAdapter
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesFragViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svMovies.isSaveEnabled = true
        viewModel.apply {
            // if Success it already has data, check not to reload everytime
            if (popularMovies.value !is NetworkResult.Success) {
                getPopularMovies()
            }
            if (topMovies.value !is NetworkResult.Success) {
                getTopRatedMovies()
            }
            if (nowPlayingMovies.value !is NetworkResult.Success) {
                getNowPlayingMovies()
            }
            if (upcomingMovies.value !is NetworkResult.Success) {
                getUpcomingMovies()
            }
        }
        setClickListeners()
        observePopular()
        observeTop()
        observeNowPlaying()
        observeUpcoming()
    }

    private fun observePopular() {
        val mAdapter = MoviesHorizontalAdapter()
        binding.rvPopular.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }

        mAdapter.onItemClick = {
            navigate(it)
        }

        viewModel.popularMovies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbPopular.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbPopular.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun observeTop() {
        val mAdapter = MoviesHorizontalAdapter()
        binding.rvTop.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }

        mAdapter.onItemClick = {
            navigate(it)
        }

        viewModel.topMovies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbTop.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbTop.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun observeNowPlaying() {
        val mAdapter = MoviesHorizontalAdapter()
        binding.rvNow.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }

        mAdapter.onItemClick = {
            navigate(it)
        }

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbNow.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbNow.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun observeUpcoming() {
        val mAdapter = MoviesHorizontalAdapter()
        binding.rvUpcoming.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }

        mAdapter.onItemClick = {
            navigate(it)
        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbUpcoming.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbUpcoming.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    // Click listeners for See All buttons
    private fun setClickListeners() {
        // ******************Click Listeners for See All buttons************
        binding.apply {
            btnSeeAllPopular.setOnClickListener {
                val action = MoviesFragmentDirections.actionMoviesFragmentToVerticalListFragment(
                    category = MyConstants.POPULAR
                )
                findNavController().navigate(action)
            }
            btnSeeAllTop.setOnClickListener {
                val action = MoviesFragmentDirections.actionMoviesFragmentToVerticalListFragment(
                    category = MyConstants.TOP_RATED
                )
                findNavController().navigate(action)
            }
            btnSeeAllNow.setOnClickListener {
                val action = MoviesFragmentDirections.actionMoviesFragmentToVerticalListFragment(
                    category = MyConstants.NOW_PLAYING
                )
                findNavController().navigate(action)
            }
            btnSeeAllUpcoming.setOnClickListener {
                val action = MoviesFragmentDirections.actionMoviesFragmentToVerticalListFragment(
                    category = MyConstants.UPCOMING
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun navigate(movie: RMovie?) {
        movie?.id?.let {
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it)
            findNavController().navigate(action)
        }
    }
}