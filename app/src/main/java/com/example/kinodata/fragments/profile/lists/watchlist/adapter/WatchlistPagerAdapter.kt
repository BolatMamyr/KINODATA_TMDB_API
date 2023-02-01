package com.example.kinodata.fragments.profile.lists.watchlist.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kinodata.fragments.profile.lists.watchlist.movies.MoviesWatchlistFragment
import com.example.kinodata.fragments.profile.lists.watchlist.tv.TvWatchlistFragment

class WatchlistPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MoviesWatchlistFragment()
            else -> TvWatchlistFragment()
        }
}