package com.example.kinodata.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.fragments.search.adapters.SearchAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentSearchBinding
import com.example.kinodata.fragments.search.adapters.PopularPersonHorizontalAdapter
import com.example.kinodata.model.multiSearch.SearchResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    // TODO: AutoCompleteTextView NEXT btn on keyboard which navigates to new search results fragment
    // TODO: Hide keyboard when AutoCompleteTextView dropdown list is touched or scrolled
    // TODO: images for recommendation boxes

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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
        binding.autoTxtSearch.maxLines = 1

        setRecommendedClickListeners()
        setSearching(view)
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

    private fun setSearching(view: View) {
        binding.imgClearIcon.setOnClickListener {
            binding.autoTxtSearch.text = SpannableStringBuilder("")
        }
        val adapter = SearchAdapter(view.context, emptyList())
        binding.autoTxtSearch.setAdapter(adapter)

        binding.autoTxtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // each time text is changed it will do new network request
                viewModel.getMultiSearchResults(p0?.toString())

                val length = p0?.length
                if (length != null) {
                    if (length > 0) {
                        binding.autoTxtSearch.textAlignment = TEXT_ALIGNMENT_TEXT_START
                        binding.imgClearIcon.visibility = VISIBLE
                    } else {
                        // if text is empty
                        binding.autoTxtSearch.textAlignment = TEXT_ALIGNMENT_CENTER
                        binding.imgClearIcon.visibility = GONE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.updateData(it.take(5))
        }

        binding.autoTxtSearch.onItemClickListener = OnItemClickListener { p0, p1, p2, p3 ->

            val item = p0?.getItemAtPosition(p2) as SearchResult

            if (item.media_type == MyConstants.MEDIA_TYPE_MOVIE) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToMovieDetailsFragment(item.id)
                findNavController().navigate(action)
            }
            if (item.media_type == MyConstants.MEDIA_TYPE_TV) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToTvDetailsFragment(item.id)
                findNavController().navigate(action)
            }
            if (item.media_type == MyConstants.MEDIA_TYPE_PERSON) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToPersonFragment(item.id)
                findNavController().navigate(action)
            }

        }
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