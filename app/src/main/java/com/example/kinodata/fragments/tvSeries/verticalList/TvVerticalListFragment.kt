package com.example.kinodata.fragments.tvSeries.verticalList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentTvVerticalListBinding
import com.example.kinodata.databinding.FragmentVerticalListBinding
import com.example.kinodata.fragments.movies.all.verticalList.VerticalListFragmentDirections
import com.example.kinodata.fragments.movies.all.verticalList.VerticalListViewModel
import com.example.kinodata.fragments.movies.all.verticalList.VerticalListViewModelFactory
import com.example.kinodata.paging.MoviesVerticalAdapter
import com.example.kinodata.paging.TvVerticalAdapter
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class TvVerticalListFragment : Fragment() {

    val args: TvVerticalListFragmentArgs by navArgs()

    private var _binding: FragmentTvVerticalListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvVerticalListViewModel by viewModels {
        TvVerticalListViewModelFactory(Repository(), args.category)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvVerticalListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbTvVerticalList.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tbTvVerticalList.title = args.category

        val adapter = TvVerticalAdapter()
        binding.rvTvVerticalList.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        lifecycleScope.launch {
            viewModel.tvSeries.collect{
                adapter.submitData(it)
            }
        }
        adapter.onItemClick = {
            it?.id?.let { tvId ->
                val action = TvVerticalListFragmentDirections
                    .actionTvVerticalListFragmentToTvDetailsFragment(tvId)
                findNavController().navigate(action)
            }

        }
    }

}