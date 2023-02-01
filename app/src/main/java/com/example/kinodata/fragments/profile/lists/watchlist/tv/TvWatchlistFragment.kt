package com.example.kinodata.fragments.profile.lists.watchlist.tv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.databinding.FragmentTvWatchlistBinding
import com.example.kinodata.fragments.profile.lists.watchlist.WatchlistFragmentDirections
import com.example.kinodata.fragments.profile.lists.watchlist.WatchlistViewModel
import com.example.kinodata.fragments.profile.lists.watchlist.adapter.TvWatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvWatchlistFragment : Fragment() {

    private var _binding: FragmentTvWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvWatchlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = TvWatchlistAdapter()
        binding.rvTvWatchlist.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getTvWatchlist()
        viewModel.tv.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collect {
                    mAdapter.submitData(it)
                }
            }
        }

        mAdapter.onItemClick = { tv ->
            tv?.id?.let { id ->
                val action = WatchlistFragmentDirections
                    .actionWatchlistFragmentToTvDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}