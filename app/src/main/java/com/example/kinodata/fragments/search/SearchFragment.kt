package com.example.kinodata.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentSearchBinding
import com.example.kinodata.fragments.search.adapters.PopularPersonHorizontalAdapter
import com.example.kinodata.fragments.search.adapters.SearchRvAdapter
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    // TODO: images for recommendation boxes

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svSearch.isSaveEnabled = true
        binding.svRecommended.isSaveEnabled = true
        binding.etSearch.maxLines = 1

        setRecommendedClickListeners()
        setSearching()
        getPopularPersons(view)
    }

    private fun getPopularPersons(view: View) {
        val mAdapter = PopularPersonHorizontalAdapter()
        binding.rvPopularPersons.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.HORIZONTAL, false
            )
        }
        viewModel.getPopularPersons()
        viewModel.popularPersons.observe(viewLifecycleOwner) {
            mAdapter.updateData(it)
        }

        mAdapter.onItemClick = {
            it?.id?.let {
                val action = SearchFragmentDirections.actionSearchFragmentToPersonFragment(it)
                findNavController().navigate(action)
            }
        }

        binding.btnSeeAllPopularPersons.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_allPopularPersonsFragment)
        }
    }

    private fun setSearching() {
        binding.imgClearIcon.setOnClickListener {
            binding.etSearch.text = SpannableStringBuilder("")
        }
        val mAdapter = SearchRvAdapter()
        binding.rvSearch.apply {
            adapter = mAdapter
            val mLayoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )
            layoutManager = mLayoutManager
            isSaveEnabled = true
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    // each time text is changed it waits 250ms to do new network request
                    delay(250L)
                    p0?.toString()?.let { viewModel.getMultiSearchResults(it) }
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                val length = p0?.length
                if (length != null) {
                    if (length > 0) {
                        showSearchResults()
                        binding.etSearch.textAlignment = TEXT_ALIGNMENT_TEXT_START
                        binding.imgClearIcon.visibility = VISIBLE
                    } else {
                        // if text is empty
                        hideSearchResults()
                        binding.etSearch.textAlignment = TEXT_ALIGNMENT_CENTER
                        binding.imgClearIcon.visibility = GONE

                        mAdapter.updateData(emptyList())
                    }
                } else {
                    hideSearchResults()
                    mAdapter.updateData(emptyList())
                }
            }
        })

        viewModel.searchResults.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data
                    mAdapter.updateData(data.results.take(20))

                    if (data.results.isEmpty()) {
                        if (binding.etSearch.text.isNullOrBlank()) {
                            hideSearchResults()
                        } else {
                            showSearchResults()
                            binding.txtNoResultsFound.visibility = View.VISIBLE
                        }
                    } else {
                        showSearchResults()
                        binding.txtNoResultsFound.visibility = View.GONE
                    }
                }
                is NetworkResult.Error -> {
                    toast("Error getting search results")
                }
                NetworkResult.Loading -> {}
            }
        }

        mAdapter.onItemClick = {
            it?.let { result ->
                if (result.media_type == MyConstants.MEDIA_TYPE_MOVIE) {
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToMovieDetailsFragment(result.id)
                    findNavController().navigate(action)
                }
                if (result.media_type == MyConstants.MEDIA_TYPE_TV) {
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToTvDetailsFragment(result.id)
                    findNavController().navigate(action)
                }
                if (result.media_type == MyConstants.MEDIA_TYPE_PERSON) {
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToPersonFragment(result.id)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun showSearchResults() {
        binding.rvSearch.visibility = View.VISIBLE
        binding.svSearch.visibility = View.GONE
    }

    private fun hideSearchResults() {
        binding.rvSearch.visibility = View.GONE
        binding.txtNoResultsFound.visibility = View.GONE
        binding.svSearch.visibility = View.VISIBLE
    }

    private fun setRecommendedClickListeners() {
        // Popular Movies
        binding.layoutRecommendedPopularMovies.setOnClickListener {
            val action = SearchFragmentDirections
                .actionSearchFragmentToVerticalListFragment(MyConstants.POPULAR)
            findNavController().navigate(action)
        }
        // Popular TV
        binding.layoutRecommendedPopularTv.setOnClickListener {
            val action = SearchFragmentDirections
                .actionSearchFragmentToTvVerticalListFragment(MyConstants.POPULAR)
            findNavController().navigate(action)
        }
        // Top Movies
        binding.layoutRecommendedTopMovies.setOnClickListener {
            val action = SearchFragmentDirections
                .actionSearchFragmentToVerticalListFragment(MyConstants.TOP_RATED)
            findNavController().navigate(action)
        }
        // Top TV
        binding.layoutRecommendedTopTv.setOnClickListener {
            val action = SearchFragmentDirections
                .actionSearchFragmentToTvVerticalListFragment(MyConstants.TOP_RATED)
            findNavController().navigate(action)
        }
    }


}