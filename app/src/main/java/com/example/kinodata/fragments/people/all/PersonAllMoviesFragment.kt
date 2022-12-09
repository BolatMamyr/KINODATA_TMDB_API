package com.example.kinodata.fragments.people.all

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
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.databinding.FragmentPersonAllMoviesBinding
import com.example.kinodata.adapters.PersonMoviesVerticalAdapter
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.fragments.people.PersonViewModelFactory
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class PersonAllMoviesFragment : Fragment() {

    private var _binding: FragmentPersonAllMoviesBinding? = null
    private val binding get() = _binding!!

    private val args: PersonAllMoviesFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView

    private val viewModel: PersonViewModel by viewModels {
        PersonViewModelFactory(Repository(), args.personId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonAllMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PersonMoviesVerticalAdapter()

        recyclerView = binding.rvPersonAllMovies
        recyclerView.apply {
            this.adapter = adapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            isSaveEnabled = true
            layoutManager = manager
            setHasFixedSize(true)
        }

        viewModel.getPersonMovieCredits()
        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        adapter.onItemClick = {
            it?.id?.let { movieId ->
                val action = PersonAllMoviesFragmentDirections
                    .actionPersonAllMoviesFragmentToMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }
        }
    }

}