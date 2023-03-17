package com.example.kinodata.fragments.movies.all.verticalList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.fragments.movies.adapters.MoviesVerticalAdapter
import com.example.kinodata.databinding.FragmentVerticalListBinding
import com.example.kinodata.repo.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VerticalListFragment"
@AndroidEntryPoint
class VerticalListFragment : Fragment() {

    // TODO: add progress bar at the end

    private val args: VerticalListFragmentArgs by navArgs()

    private var _binding: FragmentVerticalListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: Repository

    private val viewModel: VerticalListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerticalListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbVerticalList.title = args.category
        binding.tbVerticalList.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = MoviesVerticalAdapter()
        binding.rvVerticalList.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            isSaveEnabled = true
        }
        lifecycleScope.launch {
            viewModel.movies.collect {
                adapter.submitData(it)
            }
        }
        adapter.onItemClick = {
            it?.id?.let { movieId ->
                val action = VerticalListFragmentDirections
                    .actionVerticalListFragmentToMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }

        }
    }


}