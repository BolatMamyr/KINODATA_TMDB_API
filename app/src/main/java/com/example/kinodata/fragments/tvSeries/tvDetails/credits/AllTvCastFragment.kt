package com.example.kinodata.fragments.tvSeries.tvDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.adapters.CastVerticalAdapter
import com.example.kinodata.databinding.FragmentAllTvCastBinding
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTvCastFragment : Fragment() {

    val viewModel: TvDetailsViewModel by viewModels()

    private var _binding: FragmentAllTvCastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTvCastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllTvCast.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = CastVerticalAdapter()
        binding.rvAllTvCast.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }
        viewModel.getTvCredits()
        viewModel.credits.observe(viewLifecycleOwner) {
            val cast = it.cast
            adapter.updateData(cast)
        }

        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllTvCastFragmentDirections
                    .actionAllTvCastFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}