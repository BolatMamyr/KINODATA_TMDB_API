package com.example.kinodata.fragments.movies.movieDetails.review

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentReviewBinding
import com.example.kinodata.model.review.Review

class ReviewFragment : Fragment() {

    private val args: ReviewFragmentArgs by navArgs()

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val review = args.review
        binding.txtReviewAuthor.text = review.author_details.username
        binding.txtReviewDate.text = review.getDate(view.context)
        val rating = review.author_details.rating
        binding.txtReviewRating.text = review.author_details.rating.toString()
        val colorId = if(rating == .0) {
            resources.getColor(R.color.super_light_gray, null)
        } else if (rating < 5.0) {
            resources.getColor(R.color.red, null)
        } else if (rating < 7.0) {
            resources.getColor(R.color.gray, null)
        } else {
            resources.getColor(R.color.green, null)
        }
        binding.lineReview.setBackgroundColor(colorId)
        binding.layoutReviewRating.setBackgroundColor(colorId)
        binding.txtReviewContent.text = review.content

        val avatarPath = review.author_details.avatar_path
        if(avatarPath != null) {
            // may be from other websites
            if (avatarPath.contains("https")) {
                // some URL may have leading '/' symbol and it may not be found
                val index = avatarPath.indexOf("http")
                Glide.with(view.context)
                    .load(avatarPath.substring(index))
                    .into(binding.imgReviewAuthor)
            } else {
                // from TMDB pics
                Glide.with(view.context)
                    .load(MyConstants.IMG_BASE_URL + avatarPath)
                    .into(binding.imgReviewAuthor)
            }
        } else {
            binding.imgReviewAuthor.setImageResource(R.drawable.avatar)
        }

        // Close Menu item Click Listener
        binding.tbReview.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_close -> {
                    findNavController().navigateUp()
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener false
                }
            }

        }
    }

}