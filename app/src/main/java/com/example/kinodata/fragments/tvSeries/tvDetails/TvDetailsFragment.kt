package com.example.kinodata.fragments.tvSeries.tvDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.collectLatestLifecycleFlow
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvDetailsFragment : Fragment() {

    private var _binding: FragmentTvDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TvDetailsFragmentArgs by navArgs()

    private val viewModel: TvDetailsViewModel by viewModels()

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
        getAccountStates()
        addToFavorite()
        addToWatchlist()
    }

    private fun getAccountStates() {
        collectLatestLifecycleFlow(viewModel.accountState) {
            when (it) {
                is NetworkResult.Success -> {
                    val accountStates = it.data
                    val isFavorite = accountStates.favorite
                    val isInWatchlist = accountStates.watchlist

                    // Favorite
                    val imgFavorite = if (isFavorite) {
                        R.drawable.ic_favorite_red
                    } else {
                        R.drawable.ic_favorite_gray
                    }
                    binding.imgTvDetailsFavorite.setImageResource(imgFavorite)

                    // Watchlist
                    val imgWatchlist = if (isInWatchlist) {
                        R.drawable.ic_watch_later_orange
                    } else {
                        R.drawable.ic_watch_later_gray
                    }
                    binding.imgTvDetailsWatchLater.setImageResource(imgWatchlist)
                }
                else -> {}
            }
        }
    }
    private fun addToFavorite() {
        binding.imgTvDetailsFavorite.setOnClickListener {
            viewModel.addOrRemoveFromFavorite(args.tvSeriesId)
        }
        lifecycleScope.launch {
            viewModel.addToFavorite.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val message = if (it.data.status_message?.contains("deleted") == true) {
                            getString(R.string.removed_from_favorites)
                        } else {
                            getString(R.string.added_to_favorites)
                        }
                        toast(message)
                    }
                    is NetworkResult.Error -> {
                        toast(getString(R.string.something_went_wrong))
                    }
                    else -> {}
                }
            }
        }
    }
    private fun addToWatchlist() {
        binding.imgTvDetailsWatchLater.setOnClickListener {
            viewModel.addToWatchlist(args.tvSeriesId)
        }
        lifecycleScope.launch {
            viewModel.addToWatchlist.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val message = if (it.data.status_message?.contains("deleted") == true) {
                            getString(R.string.removed_from_watchlist)
                        } else {
                            getString(R.string.added_to_watchlist)
                        }
                        toast(message)
                    }
                    is NetworkResult.Error -> {
                        toast(getString(R.string.something_went_wrong))
                    }
                    else -> {}
                }
            }
        }
    }
    private fun getCredits(view: View) {
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
        collectLatestLifecycleFlow(viewModel.credits) {
            when (it) {
                is NetworkResult.Success -> {
                    val credits = it.data
                    val firstFour = credits.getFirstFourActors()

                    if (credits.cast.isNotEmpty()) {
                        binding.txtTvDetailsStars.text = if (credits.cast.size < 4) {
                                "${resources.getString(R.string.stars)} $firstFour"
                        } else {
                                "${resources.getString(R.string.stars)} $firstFour ${
                                    resources.getString(R.string.and_others)
                                }"
                        }
                    }

                    castHorizontalAdapter.updateData(credits.cast.take(12))

                    // TODO: if crew member is more popular than director it still should be after director. PUT DIRECTOR FIRST SOMEHOW OR SEPARATE FIELD FOR HIM
                    val sortedList = credits.crew.sortedByDescending { it.popularity }
                    crewHorizontalAdapter.updateData(sortedList.take(7))
                }
                else -> {}
            }

        }
        // Click Listener for See All Cast button
        binding.btnTvDetailsSeeAllCast.setOnClickListener {
            val action = TvDetailsFragmentDirections
                .actionTvDetailsFragmentToAllTvCastFragment(args.tvSeriesId)
            findNavController().navigate(action)
        }

        // Click Listener for See All Crew button
        binding.btnTvDetailsSeeAllCrew.setOnClickListener {

                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToAllTvCrewFragment(args.tvSeriesId)
                findNavController().navigate(action)

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
        collectLatestLifecycleFlow(viewModel.tvDetails) {
            when (it) {
                is NetworkResult.Success -> {
                    showUi()
                    val tvDetails = it.data
                    binding.tbTvDetails.title = tvDetails.name
                    binding.txtTvDetailsTitle.text = tvDetails.name

                    val rating = tvDetails.vote_average
                    val colorId = MyUtils.getRatingColorId(rating, view)

                    binding.txtTvDetailsVoteAve.text = String.format("%.1f", rating)
                    binding.txtTvDetailsVoteAve.setTextColor(colorId)

                    val voteCount = if (tvDetails.vote_count < 1000) {
                        tvDetails.vote_count.toString()
                    } else {
                        "${(tvDetails.vote_count / 1000)}K"
                    }
                    binding.txtTvDetailsVoteCount.text = voteCount
                    binding.txtTvDetailsTitleOriginal.text = tvDetails.original_name
                    binding.txtTvDetailsYear.text = tvDetails.first_air_date.take(4) + ","
                    binding.txtTvDetailsGenres.text = tvDetails.getGenres()
                    binding.txtTvDetailsCountry.text = tvDetails.getCountries() + ","
                    binding.txtTvDetailsDescription.text = tvDetails.overview

                    binding.txtTvDetailsVoteAveBig.text = String.format("%.1f", rating)
                    binding.txtTvDetailsVoteAveBig.setTextColor(colorId)

                    binding.txtTvDetailsVoteCountBig.text = tvDetails.vote_count.toString()

                    Glide.with(view)
                        .load(MyConstants.IMG_BASE_URL + tvDetails.poster_path)
                        .into(binding.imgTvDetailsPoster)
                }
                is NetworkResult.Error -> {
                    hideUi()
                    it.throwable.message?.let { it1 -> toast(it1) }
                }
                else -> {
                    hideUi()
                }
            }

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

        collectLatestLifecycleFlow(viewModel.reviews) {
            when (it) {
                is NetworkResult.Success -> {
                    reviewHorizontalAdapter.updateData(it.data)
                } else -> {}
            }

        }

        reviewHorizontalAdapter.onItemClick = {
            it?.let { review ->
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToReviewFragment(review)
                findNavController().navigate(action)
            }
        }

        binding.btnTvDetailsSeeAllReviews.setOnClickListener {
            val action = TvDetailsFragmentDirections.actionTvDetailsFragmentToAllReviewsFragment(
                    movieId = args.tvSeriesId.toString(),
                    context = MyConstants.TV
                )
            findNavController().navigate(action)
        }
    }

    private fun showUi() {
        binding.apply {
            pbTvDetails.visibility = View.GONE
            svTvDetails.visibility = View.VISIBLE
        }
    }

    private fun hideUi() {
        binding.apply {
            pbTvDetails.visibility = View.VISIBLE
            svTvDetails.visibility = View.GONE
        }
    }

}