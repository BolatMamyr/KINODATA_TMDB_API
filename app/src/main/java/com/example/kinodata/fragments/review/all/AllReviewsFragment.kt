package com.example.kinodata.fragments.review.all

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
import com.example.kinodata.adapters.reviews.ReviewVerticalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentAllReviewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllReviewsFragment : Fragment() {

    private var _binding: FragmentAllReviewsBinding? = null
    private val binding get() = _binding!!

    private val args: AllReviewsFragmentArgs by navArgs()
    private val viewModel: AllReviewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllReviews.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val adapter = ReviewVerticalAdapter()
        binding.rvAllReviews.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(view.context)
        }

        if (args.context == MyConstants.MOVIE) {
            viewModel.getMovieReviews()
            lifecycleScope.launch {
                viewModel.movieReviews.observe(viewLifecycleOwner) {
                    adapter.updateData(it)
                }
            }
        }

        if (args.context == MyConstants.TV) {
            viewModel.getTvReviews()
            lifecycleScope.launch {
                viewModel.tvReviews.observe(viewLifecycleOwner) {
                    adapter.updateData(it)
                }
            }
        }

        adapter.onItemClick = {
            it?.let {
                val action =
                    AllReviewsFragmentDirections.actionAllReviewsFragmentToReviewFragment(it)
                findNavController().navigate(action)
            }
        }
    }

}