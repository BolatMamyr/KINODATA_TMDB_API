package com.example.kinodata.fragments.profile.lists.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentFavoriteListBinding
import com.example.kinodata.databinding.FragmentWatchlistBinding
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoritePagerAdapter
import com.example.kinodata.fragments.profile.lists.watchlist.adapter.WatchlistPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tbWatchlist.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            viewpagerWatchlist.adapter = WatchlistPagerAdapter(this@WatchlistFragment)
            TabLayoutMediator(tabWatchlist, viewpagerWatchlist) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.movies)
                    else -> tab.text = getString(R.string.tv_series)
                }
            }.attach()
        }

    }

}