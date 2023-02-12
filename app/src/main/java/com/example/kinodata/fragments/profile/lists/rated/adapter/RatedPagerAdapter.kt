package com.example.kinodata.fragments.profile.lists.rated.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kinodata.fragments.profile.lists.rated.movies.RatedMoviesFragment
import com.example.kinodata.fragments.profile.lists.rated.tv.RatedTvFragment

class RatedPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                RatedMoviesFragment()
            }
            else -> {
                RatedTvFragment()
            }
        }
}