package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.fragments.people.all.adapters.PersonMoviesAsCrewVerticalAdapter
import com.example.kinodata.databinding.FragmentPersonAllMoviesAsCrewBinding
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonAllMoviesAsCrewFragment : Fragment() {
    private var _binding: FragmentPersonAllMoviesAsCrewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonViewModel by activityViewModels()

    private val args: PersonAllMoviesAsCrewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonAllMoviesAsCrewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllMoviesAsCrew.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (args.personId != viewModel.personId.value) {
            viewModel.getPersonInfo(args.personId)
            viewModel.getPersonMovieCredits(args.personId)
            viewModel.getPersonTvSeriesCredits(args.personId)
            viewModel.setPersonId(args.personId)
        }

        val mAdapter = PersonMoviesAsCrewVerticalAdapter()
        binding.rvAllMoviesAsCrew.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
            isSaveEnabled = true
        }
        viewModel.movies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val sortedList = it.data.crew.sortedByDescending { it.release_date }
                    mAdapter.updateData(sortedList)
                }
                else -> {

                }
            }
        }

        mAdapter.onItemClick = {
            it?.id?.let { id ->
                val action = PersonAllMoviesAsCrewFragmentDirections
                    .actionPersonAllMoviesAsCrewFragmentToMovieDetailsFragment(id)
                findNavController().navigate(action)
            }
        }
    }
}