package com.example.kinodata.fragments.tvSeries.tvDetails.credits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.adapters.credits.CastVerticalAdapter
import com.example.kinodata.databinding.FragmentAllTvCastBinding
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

// TODO: NOT SHOWING All cast and crew

private const val TAG = "AllTvCastFragment"

@AndroidEntryPoint
class AllTvCastFragment : Fragment() {

    private val viewModel: TvDetailsViewModel by viewModels()

    private var _binding: FragmentAllTvCastBinding? = null
    private val binding get() = _binding!!

    private val args: AllTvCastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTvCastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTvCredits(args.tvId.toString())
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
        viewModel.credits.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    adapter.updateData(it.data.cast)
                } else -> {}
            }

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