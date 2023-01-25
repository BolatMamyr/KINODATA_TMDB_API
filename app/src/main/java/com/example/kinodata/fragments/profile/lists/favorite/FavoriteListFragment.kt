package com.example.kinodata.fragments.profile.lists.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentFavoriteListBinding
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoritePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tbFavorite.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            viewpagerFavorite.adapter = FavoritePagerAdapter(this@FavoriteListFragment)
            TabLayoutMediator(tabFavorite, viewpagerFavorite) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.movies)
                    else -> tab.text = getString(R.string.tv_series)
                }
            }.attach()
        }

    }

}