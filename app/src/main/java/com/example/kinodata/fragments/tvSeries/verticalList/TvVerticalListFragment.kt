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
import com.example.kinodata.databinding.FragmentTvVerticalListBinding
import com.example.kinodata.fragments.tvSeries.adapters.TvVerticalAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvVerticalListFragment : Fragment() {

    val args: TvVerticalListFragmentArgs by navArgs()

    private var _binding: FragmentTvVerticalListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvVerticalListViewModel by viewModels()

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