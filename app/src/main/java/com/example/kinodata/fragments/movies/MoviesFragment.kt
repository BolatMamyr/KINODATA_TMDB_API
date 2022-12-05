package com.example.kinodata.fragments.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.R
import com.example.kinodata.adapters.MoviesHorizontalAdapter
import com.example.kinodata.databinding.FragmentMoviesBinding
import com.example.kinodata.fragments.movies.utils.MoviesFragViewModel
import com.example.kinodata.fragments.movies.utils.MoviesFragViewModelFactory
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.repo.Repository

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var rv_popular: RecyclerView
    private lateinit var popularAdapter: MoviesHorizontalAdapter

    private lateinit var rv_top: RecyclerView
    private lateinit var topAdapter: MoviesHorizontalAdapter

    private lateinit var rv_now: RecyclerView
    private lateinit var nowAdapter: MoviesHorizontalAdapter

    private lateinit var rv_upcoming: RecyclerView
    private lateinit var upcomingAdapter: MoviesHorizontalAdapter

    private lateinit var pb_popular: ProgressBar
    private lateinit var pb_top: ProgressBar
    private lateinit var pb_now: ProgressBar
    private lateinit var pb_upcoming: ProgressBar

    private lateinit var btn_seeAll_popular: Button
    private lateinit var btn_seeAll_top: Button
    private lateinit var btn_seeAll_now: Button
    private lateinit var btn_seeAll_upcoming: Button

    private val viewModel: MoviesFragViewModel by viewModels {
        MoviesFragViewModelFactory(Repository())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setClickListeners()
        getPopular()
        getTopRated()
        getNowPlaying()
        getUpcoming()
        // to save scroll position when fragment is changed
        binding.svMovies.isSaveEnabled = true
    }

    private fun getPopular() {
        viewModel.getPopularMovies(getString(R.string.language), "1")
        pb_popular.visibility = View.VISIBLE
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            popularAdapter.updateData(it)
            pb_popular.visibility = View.GONE
        })
    }

    private fun getTopRated() {
        viewModel.getTopRatedMovies(getString(R.string.language), "1")
        pb_top.visibility = View.VISIBLE
        viewModel.topMovies.observe(viewLifecycleOwner, Observer {
            topAdapter.updateData(it)
            pb_top.visibility = View.GONE
        })
    }

    private fun getNowPlaying() {
        viewModel.getNowPlayingMovies(getString(R.string.language), "1")
        pb_now.visibility = View.VISIBLE
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer {
            nowAdapter.updateData(it)
            pb_now.visibility = View.GONE
        })
    }

    private fun getUpcoming() {
        viewModel.getUpcomingMovies(getString(R.string.language), "1")
        pb_upcoming.visibility = View.VISIBLE
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
            upcomingAdapter.updateData(it)
            pb_upcoming.visibility = View.GONE
        })
    }

    // initializing all views, adapters and navController
    private fun init() {
        navController = findNavController()
        pb_popular = binding.pbPopular
        pb_top = binding.pbTop
        pb_now = binding.pbNow
        pb_upcoming = binding.pbUpcoming

        rv_popular = binding.rvPopular
        popularAdapter = MoviesHorizontalAdapter()
        rv_popular.adapter = popularAdapter
        rv_popular.isNestedScrollingEnabled = true

        rv_top = binding.rvTop
        topAdapter = MoviesHorizontalAdapter()
        rv_top.adapter = topAdapter
        rv_top.isNestedScrollingEnabled = true

        rv_now = binding.rvNow
        nowAdapter = MoviesHorizontalAdapter()
        rv_now.adapter = nowAdapter
        rv_now.isNestedScrollingEnabled = true

        rv_upcoming = binding.rvUpcoming
        upcomingAdapter = MoviesHorizontalAdapter()
        rv_upcoming.adapter = upcomingAdapter
        rv_upcoming.isNestedScrollingEnabled = true

        btn_seeAll_popular = binding.btnSeeAllPopular
        btn_seeAll_top = binding.btnSeeAllTop
        btn_seeAll_now = binding.btnSeeAllNow
        btn_seeAll_upcoming = binding.btnSeeAllUpcoming
    }

    // Click listeners for See All buttons
    private fun setClickListeners() {
        // ******************Click Listeners for See All buttons************
        btn_seeAll_popular.setOnClickListener {
            navController.navigate(R.id.action_moviesFragment_to_allPopularFragment)
        }
        btn_seeAll_top.setOnClickListener {
            navController.navigate(R.id.action_moviesFragment_to_allTopFragment)
        }
        btn_seeAll_now.setOnClickListener {
            navController.navigate(R.id.action_moviesFragment_to_allNowFragment)
        }
        btn_seeAll_upcoming.setOnClickListener {
            navController.navigate(R.id.action_moviesFragment_to_allUpcomingFragment)
        }

        // *******************Click Listeners for RV Adapters*****************
        popularAdapter.onItemClick = {
            navigate(it)
        }
        topAdapter.onItemClick = {
            navigate(it)
        }
        nowAdapter.onItemClick = {
            navigate(it)
        }
        upcomingAdapter.onItemClick = {
            navigate(it)
        }
    }

    private fun navigate(it: RMovie?) {
        val action = it?.id?.let { movieId ->
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movieId)
        }
        action?.let { it1 -> navController.navigate(it1) }
    }
}