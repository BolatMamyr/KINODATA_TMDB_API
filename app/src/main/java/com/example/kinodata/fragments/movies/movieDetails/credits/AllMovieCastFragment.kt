package com.example.kinodata.fragments.movies.movieDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.adapters.CastVerticalAdapter
import com.example.kinodata.databinding.FragmentAllMovieCastBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.utils.MyUtils.Companion.collectLatestLifecycleFlow
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMovieCastFragment : Fragment() {
    val viewModel: MovieDetailsViewModel by viewModels()

    private var _binding: FragmentAllMovieCastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMovieCastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllMovieCast.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val adapter = CastVerticalAdapter()
        binding.rvAllMovieCast.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }
        collectLatestLifecycleFlow(viewModel.credits) {
            when(it) {
                is NetworkResult.Success -> {
                    adapter.updateData(it.data.cast)
                }
                is NetworkResult.Error -> {
                    val message = it.throwable.message ?: getString(R.string.something_went_wrong)
                    toast(message)
                }
                else -> {
                    // TODO: add progressbar
                }
            }
        }

        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllMovieCastFragmentDirections
                    .actionAllMovieCastFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}