package com.example.kinodata.fragments.profile.lists.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kinodata.fragments.profile.lists.favorite.movies.FavoriteMoviesFragment
import com.example.kinodata.fragments.profile.lists.favorite.tv.FavoriteTvFragment

class FavoritePagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteMoviesFragment()
            else -> FavoriteTvFragment()
        }
    }
}