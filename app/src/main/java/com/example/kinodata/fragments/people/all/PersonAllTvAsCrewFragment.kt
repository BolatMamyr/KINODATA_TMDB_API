package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.adapters.PersonTvAsCrewVerticalAdapter
import com.example.kinodata.databinding.FragmentPersonAllTvAsCrewBinding
import com.example.kinodata.fragments.people.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAllTvAsCrewFragment : Fragment() {
    private var _binding: FragmentPersonAllTvAsCrewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonAllTvAsCrewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllTvAsCrew.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        val mAdapter = PersonTvAsCrewVerticalAdapter()
        binding.rvAllTvAsCrew.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
        }
        viewModel.getPersonTvSeriesCredits()
        viewModel.tvAsCrew.observe(viewLifecycleOwner) {
            val sortedList = it.sortedByDescending { it.first_air_date }
            mAdapter.updateData(sortedList)
        }

        mAdapter.onItemClick = {
            it?.id?.let { id ->
                val action = PersonAllTvAsCrewFragmentDirections
                    .actionPersonAllTvAsCrewFragmentToTvDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }

}