package com.example.kinodata.fragments.movies.all.popular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.paging.MoviesVerticalAdapter
import com.example.kinodata.databinding.FragmentAllPopularBinding
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class AllPopularFragment : Fragment() {

    // TODO: add progress bar at the end

    private var _binding: FragmentAllPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PopularViewModel by viewModels {
        PopularViewModelFactory(Repository())
    }

    private lateinit var recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllPopularBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvAllPopular
        val adapter = MoviesVerticalAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        lifecycleScope.launch {
            viewModel.movies.collect{
                adapter.submitData(it)
            }
        }
        adapter.onItemClick = {
            val action = it?.id?.let { movieId ->
                AllPopularFragmentDirections
                    .actionAllPopularFragmentToMovieDetailsFragment(movieId)
            }
            action?.let { it1 -> findNavController().navigate(it1) }
        }
    }


}