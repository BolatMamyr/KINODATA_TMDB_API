package com.example.kinodata.fragments.profile.lists.rated

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentFavoriteListBinding
import com.example.kinodata.databinding.FragmentRatedListBinding
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoritePagerAdapter
import com.example.kinodata.fragments.profile.lists.rated.adapter.RatedPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatedListFragment : Fragment() {

    private var _binding: FragmentRatedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatedListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tbRated.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            viewpagerRated.adapter = RatedPagerAdapter(this@RatedListFragment)
            TabLayoutMediator(tabRated, viewpagerRated) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.movies)
                    else -> tab.text = getString(R.string.tv_series)
                }
            }.attach()
        }
    }

}