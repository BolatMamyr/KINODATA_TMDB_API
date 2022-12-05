package com.example.kinodata.fragments.movies.movieDetails.review.all

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
import com.example.kinodata.paging.ReviewVerticalAdapter
import com.example.kinodata.databinding.FragmentAllReviewsBinding
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllReviewsFragment : Fragment() {

    private var _binding: FragmentAllReviewsBinding? = null
    private val binding get() = _binding!!

    private val args: AllReviewsFragmentArgs by navArgs()
    private val viewModel: ReviewsViewModel by viewModels {
        ReviewsViewModelFactory(Repository(), args.movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ReviewVerticalAdapter()
        binding.rvAllReviews.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(view.context)
        }

        viewModel.getReviews()
        lifecycleScope.launch {
            viewModel.reviews.observe(viewLifecycleOwner) {
                adapter.updateData(it)
            }
        }

        adapter.onItemClick = {
            it?.let {
                val action = AllReviewsFragmentDirections
                    .actionAllReviewsFragmentToReviewFragment(it)
                findNavController().navigate(action)
            }
        }
    }

}