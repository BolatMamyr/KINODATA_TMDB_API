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
import com.example.kinodata.adapters.credits.CrewVerticalAdapter
import com.example.kinodata.databinding.FragmentAllTvCrewBinding
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

// TODO: NOT SHOWING All cast and crew

private const val TAG = "AllTvCrewFragment"

@AndroidEntryPoint
class AllTvCrewFragment : Fragment() {

    private val viewModel: TvDetailsViewModel by viewModels()

    private var _binding: FragmentAllTvCrewBinding? = null
    private val binding get() = _binding!!

    private val args: AllTvCrewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTvCrewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTvCredits(args.tvId.toString())
        binding.tbAllTvCrew.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = CrewVerticalAdapter()
        binding.rvAllTvCrew.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            isSaveEnabled = true
        }

        viewModel.credits.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    val sortedList = it.data.crew.sortedByDescending { it.popularity }
                    adapter.updateData(sortedList)
                }
                else -> {}
            }

        }
        adapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllTvCrewFragmentDirections
                    .actionAllTvCrewFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}