package com.example.kinodata.fragments.tvSeries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentTvSeriesBinding
import com.example.kinodata.fragments.tvSeries.adapters.TvHorizontalAdapter
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeriesFragment : Fragment() {

    // TODO: See All Reviews

    private var _binding: FragmentTvSeriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvSeriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvSeriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svTvSeries.isSaveEnabled = true
        viewModel.apply {
            // if Success it already has data, check not to reload everytime
            if (popularTv.value !is NetworkResult.Success) {
                getPopularTv()
            }
            if (topTv.value !is NetworkResult.Success) {
                getTopTv()
            }
            if (airingTv.value !is NetworkResult.Success) {
                getAiringTv()
            }
        }
        observePopular()
        observeTop()
        observeAiring()
        setClickListeners()
    }

    private fun observePopular() {
        val mAdapter = TvHorizontalAdapter()
        binding.rvTvPopular.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }
        mAdapter.onItemClick = {
            navigateToTvDetails(it)
        }
        viewModel.popularTv.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbTvPopular.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbTvPopular.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun observeTop() {
        val mAdapter = TvHorizontalAdapter()
        binding.rvTvTop.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }
        mAdapter.onItemClick = {
            navigateToTvDetails(it)
        }
        viewModel.topTv.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbTvTop.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbTvTop.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun observeAiring() {
        val mAdapter = TvHorizontalAdapter()
        binding.rvTvAiring.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }
        mAdapter.onItemClick = {
            navigateToTvDetails(it)
        }
        viewModel.airingTv.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.pbTvAiring.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    mAdapter.updateData(it.data.results)
                    binding.pbTvAiring.visibility = View.GONE
                }
                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun setClickListeners() {
        binding.btnTvSeeAllPopular.setOnClickListener {
            navigateToVerticalList(MyConstants.POPULAR)
        }

        binding.btnTvSeeAllTop.setOnClickListener {
            navigateToVerticalList(MyConstants.TOP_RATED)
        }

        binding.btnTvSeeAllAiring.setOnClickListener {
            navigateToVerticalList(MyConstants.NOW_PLAYING)
        }
    }

    private fun navigateToTvDetails(tv: RTvSeries?) {
        tv?.id?.let {
            val action = TvSeriesFragmentDirections
                .actionTvSeriesFragmentToTvSeriesDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun navigateToVerticalList(category: String) {
            val action = TvSeriesFragmentDirections
                .actionTvSeriesFragmentToTvVerticalListFragment(category)
            findNavController().navigate(action)
    }
}