package com.example.kinodata.fragments.movies.all.topRated

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
import com.example.kinodata.R
import com.example.kinodata.paging.MoviesVerticalAdapter
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class AllTopFragment : Fragment() {
    // TODO: add progress bar at the end
    private lateinit var recyclerView: RecyclerView
    private val viewModel: TopRatedViewModel by viewModels {
        TopRatedViewModelFactory(Repository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_allTop)

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
                AllTopFragmentDirections.actionAllTopFragmentToMovieDetailsFragment(movieId)
            }
            action?.let { it1 -> findNavController().navigate(it1) }
        }
    }
}