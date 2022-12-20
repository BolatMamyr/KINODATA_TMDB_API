package com.example.kinodata.fragments.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kinodata.R
import com.example.kinodata.adapters.SearchAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentSearchBinding
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.repo.Repository

class SearchFragment : Fragment() {

    // TODO: make dropdown items fancier: with media type, rating overview, birthday(age) etc.
    // TODO: AutoCompleteTextView NEXT btn on keyboard which navigates to new search results fragment
    // TODO: Hide keyboard when AutoCompleteTextView dropdown list is touched or scrolled
    // TODO: images for recommendation boxes
    // TODO: Popular people RV

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(Repository())
    }
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
                        binding.imgClearIcon.visibility = View.VISIBLE
                    } else {
                        // if text is empty
                        binding.autoTxtSearch.textAlignment = TEXT_ALIGNMENT_CENTER
                        binding.imgClearIcon.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        binding.autoTxtSearch.onItemClickListener = OnItemClickListener { p0, p1, p2, p3 ->

            val item = p0?.getItemAtPosition(p2) as SearchResult

            if (item.media_type == MyConstants.movie) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToMovieDetailsFragment(item.id)
                findNavController().navigate(action)
            }
            if (item.media_type == MyConstants.tv) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToTvDetailsFragment(item.id)
                findNavController().navigate(action)
            }
            if (item.media_type == MyConstants.person) {
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