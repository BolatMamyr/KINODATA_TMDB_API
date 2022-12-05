package com.example.kinodata.fragments.tvSeries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.adapters.TvSeriesHorizontalAdapter
import com.example.kinodata.databinding.FragmentTvSeriesBinding
import com.example.kinodata.repo.Repository

class TvSeriesFragment : Fragment() {

    private var _binding: FragmentTvSeriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvSeriesViewModel by viewModels {
        TvSeriesViewModelFactory(Repository())
    }

    private lateinit var popularAdapter: TvSeriesHorizontalAdapter
    private lateinit var topAdapter: TvSeriesHorizontalAdapter
    private lateinit var airingAdapter: TvSeriesHorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvSeriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularAdapter = TvSeriesHorizontalAdapter()
        topAdapter = TvSeriesHorizontalAdapter()
        airingAdapter = TvSeriesHorizontalAdapter()

        setUpRVs()
        getPopular()
        getTopRated()
        getAiring()
    }

    private fun setUpRVs() {
        // using binding directly not working while setting it up
        val rvPopular = binding.rvTvPopular
        rvPopular.apply {
            this.adapter = popularAdapter
            val manager = LinearLayoutManager(this@TvSeriesFragment.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isNestedScrollingEnabled = true
        }

        val rvTop = binding.rvTvTop
        rvTop.apply {
            this.adapter = topAdapter
            val manager = LinearLayoutManager(this@TvSeriesFragment.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isNestedScrollingEnabled = true
        }

        val rvAiring = binding.rvTvAiring
        rvAiring.apply {
            this.adapter = airingAdapter
            val manager = LinearLayoutManager(this@TvSeriesFragment.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isNestedScrollingEnabled = true
        }
    }

    private fun getPopular() {
        viewModel.getPopularTvSeries(language = getString(R.string.language), page = "1")
        binding.pbTvPopular.visibility = View.VISIBLE
        viewModel.popularTvSeries.observe(viewLifecycleOwner) {
            popularAdapter.updateData(it)
            binding.pbTvPopular.visibility = View.GONE
        }
    }

    private fun getTopRated() {
        viewModel.getTopRatedTvSeries(language = getString(R.string.language), page = "1")
        binding.pbTvTop.visibility = View.VISIBLE
        viewModel.topRatedTvSeries.observe(viewLifecycleOwner) {
            topAdapter.updateData(it)
            binding.pbTvTop.visibility = View.GONE
        }
    }

    private fun getAiring() {
        viewModel.getAiringTvSeries(language = getString(R.string.language), page = "1")
        binding.pbTvAiring.visibility = View.VISIBLE
        viewModel.airingTvSeries.observe(viewLifecycleOwner) {
            airingAdapter.updateData(it)
            binding.pbTvAiring.visibility = View.GONE
        }
    }



}