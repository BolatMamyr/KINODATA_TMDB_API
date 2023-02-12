package com.example.kinodata.fragments.profile.lists.rated.tv

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
import com.example.kinodata.databinding.FragmentFavoriteTvBinding
import com.example.kinodata.databinding.FragmentRatedTvBinding
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteListFragmentDirections
import com.example.kinodata.fragments.profile.lists.favorite.FavoriteViewModel
import com.example.kinodata.fragments.profile.lists.favorite.adapter.FavoriteTvAdapter
import com.example.kinodata.fragments.profile.lists.rated.RatedListFragmentDirections
import com.example.kinodata.fragments.profile.lists.rated.RatedViewModel
import com.example.kinodata.fragments.profile.lists.rated.adapter.RatedTvAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RatedTvFragment : Fragment() {

    private var _binding: FragmentRatedTvBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RatedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatedTvBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = RatedTvAdapter()
        binding.rvRatedTv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getRatedTv()
        viewModel.ratedTv.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                it.flow.collectLatest {
                    mAdapter.submitData(it)
                }
            }
        }

        mAdapter.onItemClick = { tv ->
            tv?.id?.let { id ->
                val action = RatedListFragmentDirections
                    .actionRatedListFragmentToTvDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}