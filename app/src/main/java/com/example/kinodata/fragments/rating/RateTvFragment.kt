package com.example.kinodata.fragments.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentRateBinding
import com.example.kinodata.databinding.FragmentRateTvBinding
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.collectLatestLifecycleFlow
import com.example.kinodata.utils.NetworkResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateTvFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRateTvBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvDetailsViewModel by activityViewModels()
    private var ratingByUser = .0
    private var toRate = 7.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRateTvBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Expand to see all views
        val bottomSheet = dialog as BottomSheetDialog
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.tbBsRateTv.setOnMenuItemClickListener {
            findNavController().navigateUp()
        }
        viewModel.changeToRateValue(7.0)
        observeRatingByUser()
        incrementRating()
        subtractRating()
        setImage()
        rate()
        collectToRate()
        deleteRating()
    }

    private fun rate() {
        binding.btnBsRateTv.setOnClickListener {
            // if it is on number already rated by user on Clicking "Rate" btn nothing happens
            if (toRate == ratingByUser) {
                findNavController().navigateUp()
            } else {
                viewModel.rate(toRate)
                findNavController().navigateUp()
            }
        }
    }

    private fun deleteRating() {
        binding.btnBsDeleteTvRating.setOnClickListener {
            viewModel.deleteRating()
            findNavController().navigateUp()
        }
    }

    private fun setImage() {
        viewModel.tvDetails.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val posterPath = it.data.poster_path
                    Glide.with(requireContext())
                        .load(MyConstants.IMG_BASE_URL + posterPath)
                        .into(binding.imgBsRateTv)
                }
                is NetworkResult.Error -> {
                    binding.imgBsRateTv.setImageResource(R.drawable.ic_movie)
                }
                else -> {}
            }
        }
    }

    private fun observeRatingByUser() {
        viewModel.ratingByUser.observe(viewLifecycleOwner) { rating ->
            if (rating > 0) {
                // if it is more than zero then it's rated by user. toRate value in ViewModel
                // needs to be changed to be collected in collectToRate() function.
                viewModel.changeToRateValue(rating)
                ratingByUser = rating
                binding.btnBsDeleteTvRating.visibility = View.VISIBLE
            } else {
                binding.btnBsDeleteTvRating.visibility = View.GONE
            }
        }
    }

    // current val which represents UI state. if it is rated by user it will get that value, if not
    // by default will be equal to 7.0. Changes with incrementRating() and subtractRating()
    // functions of ViewModel
    private fun collectToRate() {
        collectLatestLifecycleFlow(viewModel.toRate) {
            binding.txtBsRatingTv.apply {
                toRate = it
                val colorId = MyUtils.getRatingColorId(it, requireView())
                setTextColor(colorId)
                text = it.toString()
            }
        }
    }

    private fun incrementRating() {
        binding.btnBsRateTvPlus.setOnClickListener {
            viewModel.incrementRating()
        }
    }

    private fun subtractRating() {
        binding.btnBsRateTvMinus.setOnClickListener {
            viewModel.subtractRating()
        }
    }
}