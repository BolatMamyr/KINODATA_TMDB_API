package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.fragments.people.all.adaptersVertical.PersonTvAsCrewVerticalAdapter
import com.example.kinodata.databinding.FragmentPersonAllTvAsCrewBinding
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAllTvAsCrewFragment : Fragment() {
    private var _binding: FragmentPersonAllTvAsCrewBinding? = null
    private val binding get() = _binding!!

    private val args: PersonAllTvAsCrewFragmentArgs by navArgs()
    private val viewModel: PersonViewModel by activityViewModels()

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

        if (args.personId != viewModel.personId.value) {
            viewModel.getPersonInfo(args.personId)
            viewModel.getPersonMovieCredits(args.personId)
            viewModel.getPersonTvSeriesCredits(args.personId)
            viewModel.setPersonId(args.personId)
        }

        val mAdapter = PersonTvAsCrewVerticalAdapter()
        binding.rvAllTvAsCrew.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
        }

        viewModel.tv.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val sortedList = it.data.crew.sortedByDescending { it.first_air_date }
                    mAdapter.updateData(sortedList)
                }
                else -> {

                }
            }
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