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
import com.example.kinodata.R
import com.example.kinodata.adapters.PersonActingMoviesVerticalAdapter
import com.example.kinodata.adapters.PersonMoviesAsCrewVerticalAdapter
import com.example.kinodata.databinding.FragmentPersonAllMoviesAsCrewBinding
import com.example.kinodata.databinding.FragmentSearchBinding
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.fragments.people.PersonViewModelFactory
import com.example.kinodata.repo.Repository

class PersonAllMoviesAsCrewFragment : Fragment() {
    private var _binding: FragmentPersonAllMoviesAsCrewBinding? = null
    private val binding get() = _binding!!

    private val args: PersonAllMoviesAsCrewFragmentArgs by navArgs()

    private val viewModel: PersonViewModel by viewModels {
        PersonViewModelFactory(Repository(), args.personId.toString())
    }

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


        val mAdapter = PersonMoviesAsCrewVerticalAdapter()
        binding.rvAllMoviesAsCrew.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
        }
        viewModel.getPersonMovieCredits()
        viewModel.moviesAsCrew.observe(viewLifecycleOwner) {
            val sortedList = it.sortedByDescending { it.release_date }
            mAdapter.updateData(sortedList)
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