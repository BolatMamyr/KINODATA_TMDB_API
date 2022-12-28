package com.example.kinodata.fragments.tvSeries.tvDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.adapters.CastHorizontalAdapter
import com.example.kinodata.adapters.CrewHorizontalAdapter
import com.example.kinodata.adapters.ReviewHorizontalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentTvDetailsBinding
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.MyUtils

class TvDetailsFragment : Fragment() {

    private var _binding: FragmentTvDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TvDetailsFragmentArgs by navArgs()

    private val viewModel: TvDetailsViewModel by viewModels {
        TvDetailsViewModelFactory(Repository(), args.tvSeriesId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbTvDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.svTvDetails.isSaveEnabled = true
        getTvDetails(view)
        getCredits(view)
        getReviews(view)


    }

    private fun getCredits(view: View) {
        viewModel.getTvCredits()

        val castHorizontalAdapter = CastHorizontalAdapter()
        val crewHorizontalAdapter = CrewHorizontalAdapter()

        binding.rvTvDetailsCast.apply {
            adapter = castHorizontalAdapter
            val manager = GridLayoutManager(view.context, 4)
            manager.orientation = RecyclerView.HORIZONTAL
            layoutManager = manager
        }
        binding.rvTvDetailsCrew.apply {
            adapter = crewHorizontalAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = RecyclerView.HORIZONTAL
            layoutManager = manager
        }
        viewModel.credits.observe(viewLifecycleOwner) {
            val firstFour = it.getFirstFourActors()
            binding.txtTvDetailsStars.text =
                "${resources.getString(R.string.stars)} $firstFour ${resources.getString(R.string.and_others)}"

            if (it.cast.isNotEmpty()) {
                if (it.cast.size < 4) {
                    binding.txtTvDetailsStars.text = "${resources.getString(R.string.stars)} $firstFour"
                } else {
                    binding.txtTvDetailsStars.text = "${resources.getString(R.string.stars)} $firstFour ${resources.getString(R.string.and_others)}"
                }
            }

            castHorizontalAdapter.updateData(it.cast.take(12))

            // TODO: if crew member is more popular than director it still should be after director. PUT DIRECTOR FIRST SOMEHOW OR SEPARATE FIELD FOR HIM
            val sortedList = it.crew.sortedByDescending { it.popularity }
            crewHorizontalAdapter.updateData(sortedList.take(7))
        }
        // Click Listener for See All Cast button
        binding.btnTvDetailsSeeAllCast.setOnClickListener {
            viewModel.credits.value?.id?.let {
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToAllTvCastFragment(it)
                findNavController().navigate(action)
            }
        }

        // Click Listener for See All Crew button
        binding.btnTvDetailsSeeAllCrew.setOnClickListener {
            viewModel.credits.value?.id?.let {
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToAllTvCrewFragment(it)
                findNavController().navigate(action)
            }
        }

        // Click Listener for cast RV item
        castHorizontalAdapter.onItemClick = {
            it?.id?.let { castId ->
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToPersonFragment(castId)
                findNavController().navigate(action)
            }
        }

        // Click Listener for crew RV item
        crewHorizontalAdapter.onItemClick = {
            it?.id?.let { castId ->
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToPersonFragment(castId)
                findNavController().navigate(action)
            }
        }
    }

    private fun getTvDetails(view: View) {
        viewModel.getTvDetails()
        viewModel.tvSeries.observe(viewLifecycleOwner) {
            binding.tbTvDetails.title = it.name
            binding.txtTvDetailsTitle.text = it.name

            val rating = it.vote_average
            val colorId = MyUtils.getRatingColorId(rating, view)

            binding.txtTvDetailsVoteAve.text = String.format("%.1f", rating)
            binding.txtTvDetailsVoteAve.setTextColor(colorId)

            val voteCount = if (it.vote_count < 1000) {
                it.vote_count.toString()
            } else {
                "${(it.vote_count / 1000)}K"
            }
            binding.txtTvDetailsVoteCount.text = voteCount
            binding.txtTvDetailsTitleOriginal.text = it.original_name
            binding.txtTvDetailsYear.text = it.first_air_date.take(4) + ","
            binding.txtTvDetailsGenres.text = it.getGenres()
            binding.txtTvDetailsCountry.text = it.getCountries() + ","
            binding.txtTvDetailsDescription.text = it.overview

            binding.txtTvDetailsVoteAveBig.text = String.format("%.1f", rating)
            binding.txtTvDetailsVoteAveBig.setTextColor(colorId)

            binding.txtTvDetailsVoteCountBig.text = it.vote_count.toString()

            Glide.with(view)
                .load(MyConstants.IMG_BASE_URL + it.poster_path)
                .into(binding.imgTvDetailsPoster)
        }
    }

    private fun getReviews(view: View) {
        val reviewHorizontalAdapter = ReviewHorizontalAdapter()
        binding.rvTvDetailsReviews.apply {
            adapter = reviewHorizontalAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isSaveEnabled = true
        }
        viewModel.getTvReviews()

        viewModel.reviews.observe(viewLifecycleOwner) {
            it?.let { list -> reviewHorizontalAdapter.updateData(list) }
        }

        reviewHorizontalAdapter.onItemClick = {
            it?.let { review ->
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToReviewFragment(review)
                findNavController().navigate(action)
            }
        }

        binding.btnTvDetailsSeeAllReviews.setOnClickListener {
            val action = TvDetailsFragmentDirections
                .actionTvDetailsFragmentToAllReviewsFragment(
                    movieId = args.tvSeriesId.toString(), context = MyConstants.TV
                )
            findNavController().navigate(action)
        }
    }

}