package com.example.kinodata.fragments.profile.lists.favorite.tv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentFavoriteMoviesBinding
import com.example.kinodata.databinding.FragmentFavoriteTvBinding
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteListFragmentDirections
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteViewModel
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoriteMoviesAdapter
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoriteTvAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteTvFragment : Fragment() {

    private var _binding: FragmentFavoriteTvBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTvBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = FavoriteTvAdapter()
        binding.rvFavoriteTv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getFavoriteTv()
        viewModel.favoriteTv.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collect {
                    mAdapter.submitData(it)
                }
            }
        }

        mAdapter.onItemClick = { tv ->
            tv?.id?.let { id ->
                val action = FavoriteListFragmentDirections
                    .actionFavoriteListFragmentToTvDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}