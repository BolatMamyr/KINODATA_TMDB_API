package com.example.kinodata.fragments.tvSeries.tvDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.adapters.credits.CastHorizontalAdapter
import com.example.kinodata.adapters.credits.CrewHorizontalAdapter
import com.example.kinodata.adapters.reviews.ReviewHorizontalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentTvDetailsBinding
import com.example.kinodata.fragments.image.adapters.ImagesAdapter
import com.example.kinodata.fragments.tvSeries.adapters.TvHorizontalAdapter
import com.example.kinodata.fragments.tvSeries.adapters.TvSeasonsAdapter
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TvDetailsFragment"
@AndroidEntryPoint
class TvDetailsFragment : Fragment() {

    private var _binding: FragmentTvDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TvDetailsFragmentArgs by navArgs()

    private val viewModel: TvDetailsViewModel by activityViewModels()

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

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

        if (args.tvSeriesId != viewModel.tvId.value) {
            viewModel.getTvDetails(args.tvSeriesId)
            viewModel.getTvCredits(args.tvSeriesId.toString())
            viewModel.getTvReviews(args.tvSeriesId.toString())
            viewModel.getTvImages(args.tvSeriesId)
            viewModel.getTvAccountStates(args.tvSeriesId)
            viewModel.getTvRecommendations(args.tvSeriesId)
            viewModel.setTvId(args.tvSeriesId)
        }

        binding.svTvDetails.isSaveEnabled = true
        observeTvDetails(view)
        observeCredits(view)
        observeReviews(view)
        observeImages()
        observeRecommendations()
        observeIsFavorite()
        observeIsInWatchlist()
        observeRatingByUser()

        rateTv()
        addToFavorite()
        addToWatchlist()
        collectRateResult()
        collectDeleteRatingResult()

    }

    private fun observeRecommendations() {
        viewModel.recommendations.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Loading ->
                    binding.layoutTvDetailsRecommendations.visibility = View.GONE

                is NetworkResult.Success -> {
                    if (it.data.results.isEmpty()) {
                        binding.layoutTvDetailsRecommendations.visibility = View.GONE
                    } else {
                        binding.layoutTvDetailsRecommendations.visibility = View.VISIBLE
                        val mAdapter = TvHorizontalAdapter()
                        binding.rvTvDetailsRecommendations.apply {
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            isSaveEnabled = true
                            isNestedScrollingEnabled = true
                        }
                        mAdapter.onItemClick = {
                            it?.id?.let { id ->
                                val action = TvDetailsFragmentDirections
                                    .actionTvDetailsFragmentSelf(id)
                                findNavController().navigate(action)
                            }
                        }
                        mAdapter.updateData(it.data.results)
                    }

                }
                is NetworkResult.Error ->
                    binding.layoutTvDetailsRecommendations.visibility = View.GONE
            }
        }
    }

    private fun observeImages() {
        val mAdapter = ImagesAdapter()

        binding.rvTvDetailsImages.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
            isSaveEnabled = true
            isNestedScrollingEnabled = true
        }

        mAdapter.onItemClick = {
            it?.let { imgNumber ->
                val action = TvDetailsFragmentDirections
                    .actionTvDetailsFragmentToTvFullImageFragment(imgNumber)
                findNavController().navigate(action)
            }
        }
        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.layoutTvDetailsImages.visibility = View.GONE
                }
                is NetworkResult.Success -> {
                    if (it.data.backdrops.isEmpty()) {
                        binding.layoutTvDetailsImages.visibility = View.GONE
                    } else {
                        val data = it.data.backdrops.map { it.file_path }.take(10)
                        mAdapter.updateData(data)
                        binding.layoutTvDetailsImages.visibility = View.VISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    binding.layoutTvDetailsImages.visibility = View.GONE
                }
            }
        }

        binding.btnTvDetailsSeeAllImages.setOnClickListener {
            val action = TvDetailsFragmentDirections
                .actionTvDetailsFragmentToAllTvImagesFragment()
            findNavController().navigate(action)
        }
    }

    private fun collectDeleteRatingResult() {
        lifecycleScope.launch {
            viewModel.deleteRating.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val success = it.data.success
                        if (success) {
                            toast(getString(R.string.rating_deleted_successfully))
                        } else {
                            toast(getString(R.string.couldntDeleteRating))
                        }
                    }
                    is NetworkResult.Error -> {
                        toast(getString(R.string.couldntDeleteRating))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeRatingByUser() {
        viewModel.ratingByUser.observe(viewLifecycleOwner) { rating ->
            if (rating > 0) {
                showRatingByUserUi()
                val colorId = MyUtils.getRatingColorId(rating, requireView())
                binding.apply {
                    cardTvRatingByUser.setCardBackgroundColor(colorId)
                    txtTvRatingByUser.text = rating.toString()

                    txtTvRateButtonRating.text = rating.toString()
                }
            } else {
                hideRatingByUserUi()
            }
        }
    }

    private fun collectRateResult() {
        lifecycleScope.launch {
            viewModel.rate.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data
                        if (response.success) {
                            toast(getString(R.string.rated_successfully))
                        } else {
                            toast(getString(R.string.couldntRateTv))
                        }
                    }
                    is NetworkResult.Error -> {
                        toast(getString(R.string.couldntRateTv))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun rateTv() {
        binding.btnTvDetailsRate.setOnClickListener {
            lifecycleScope.launch {
                dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                    if (!isSignedIn) {
                        toast(getString(R.string.please_sign_in))
                    } else {
                        val action = TvDetailsFragmentDirections
                            .actionTvDetailsFragmentToRateTvFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun observeIsInWatchlist() {
        viewModel.isInWatchlist.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val inWatchlist = it.data
                    val img = if (inWatchlist) {
                        R.drawable.ic_watch_later_orange
                    } else {
                        R.drawable.ic_watch_later_gray
                    }
                    binding.imgTvDetailsWatchLater.setImageResource(img)
                    binding.imgTvDetailsWatchLater.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    binding.imgTvDetailsWatchLater.setImageResource(R.drawable.ic_watch_later_gray)
                    binding.imgTvDetailsWatchLater.visibility = View.VISIBLE
                }
                else -> {
                    binding.imgTvDetailsWatchLater.visibility = View.GONE
                }
            }
        }
    }

    private fun observeIsFavorite() {
        viewModel.isFavorite.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val isFavorite = it.data
                    val img = if (isFavorite) {
                        R.drawable.ic_favorite_red
                    } else {
                        R.drawable.ic_favorite_gray
                    }
                    binding.imgTvDetailsFavorite.setImageResource(img)
                    binding.imgTvDetailsFavorite.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    binding.imgTvDetailsFavorite.setImageResource(R.drawable.ic_favorite_gray)
                    binding.imgTvDetailsFavorite.visibility = View.VISIBLE
                }
                else -> {
                    binding.imgTvDetailsFavorite.visibility = View.GONE
                }
            }
        }
    }

    private fun addToFavorite() {
        binding.imgTvDetailsFavorite.setOnClickListener {
            viewModel.addToFavorite(args.tvSeriesId)
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
                        val msg = it.throwable.message ?: getString(R.string.couldntMarkAsFavorite)
                        toast(msg)
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
                        val msg = it.throwable.message ?: getString(R.string.couldntAddToWatchlist)
                        toast(msg)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeCredits(view: View) {
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

    private fun observeTvDetails(view: View) {
        viewModel.tvDetails.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val tvDetails = it.data
                        tbTvDetails.title = tvDetails.name
                        txtTvDetailsTitle.text = tvDetails.name

                        val rating = tvDetails.vote_average
                        val colorId = MyUtils.getRatingColorId(rating, view)

                        txtTvDetailsVoteAve.text = String.format("%.1f", rating)
                        txtTvDetailsVoteAve.setTextColor(colorId)

                        val voteCount = if (tvDetails.vote_count < 1000) {
                            tvDetails.vote_count.toString()
                        } else {
                            "${(tvDetails.vote_count / 1000)}K"
                        }
                        txtTvDetailsVoteCount.text = voteCount
                        txtTvDetailsTitleOriginal.text = tvDetails.original_name

                        // short details on top
                        val year = tvDetails.first_air_date.take(4)
                        val genres = tvDetails.getGenres()
                        val countries = tvDetails.getCountries(requireContext())
                        val seasons = if (tvDetails.number_of_seasons > 1) {
                            getString(R.string.seasons)
                        } else {
                            getString(R.string.season)
                        }
                        val numberOfSeasons = ", ${tvDetails.number_of_seasons} $seasons"
                        val minutes = getString(R.string.minutes)
                        val episodeRuntime = if(tvDetails.episode_run_time.isNotEmpty()) {
                            ", ${tvDetails.episode_run_time.average().toInt()} $minutes"
                        } else {
                            ""
                        }
                        val shortDetails = "$year, $genres, $countries$numberOfSeasons$episodeRuntime"

                        txtTvDetailsDetailsOnTop.text = shortDetails

                        txtTvDetailsDescription.text = tvDetails.overview

                        txtTvDetailsVoteAveBig.text = String.format("%.1f", rating)
                        txtTvDetailsVoteAveBig.setTextColor(colorId)

                        txtTvDetailsVoteCountBig.text = tvDetails.vote_count.toString()

                        Glide.with(view)
                            .load(MyConstants.IMG_BASE_URL + tvDetails.poster_path)
                            .into(binding.imgTvDetailsPoster)

                        // Seasons
                        val seasonsList = tvDetails.seasons
                        val mAdapter = TvSeasonsAdapter(seasonsList)
                        rvTxtTvDetailsSeasons.apply {
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                                )
                            isSaveEnabled = true
                            isNestedScrollingEnabled = true
                        }
                        mAdapter.onItemClick = {
                            if (it != null) {
                                val action = TvDetailsFragmentDirections
                                    .actionTvDetailsFragmentToSeasonDetailsFragment(
                                        tvDetails.name, args.tvSeriesId, it.name, it.season_number
                                    )
                                findNavController().navigate(action)
                            }

                        }

                        showUi()
                    }
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

    private fun observeReviews(view: View) {
        val reviewHorizontalAdapter = ReviewHorizontalAdapter()
        binding.rvTvDetailsReviews.apply {
            adapter = reviewHorizontalAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isSaveEnabled = true
        }
        viewModel.reviews.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    reviewHorizontalAdapter.updateData(it.data)
                }
                else -> {}
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

    private fun hideRatingByUserUi() {
        binding.apply {
            cardTvRatingByUser.visibility = View.GONE

            btnTvDetailsRate.setCardBackgroundColor(resources.getColor(R.color.orange, null))
            txtTvRateButton.apply {
                text = getString(R.string.rate)
                setTextColor(resources.getColor(R.color.white, null))
            }
            cardTvRateButtonRating.visibility = View.GONE
        }
    }

    private fun showRatingByUserUi() {
        binding.apply {
            // card showing rating by user on top next to voteCount
            cardTvRatingByUser.visibility = View.VISIBLE

            // changes rate button UI to "update rating" button UI
            btnTvDetailsRate.setCardBackgroundColor(resources.getColor(R.color.white, null))
            txtTvRateButton.apply {
                text = getString(R.string.update_rating)
                setTextColor(resources.getColor(R.color.black, null))
            }
            cardTvRateButtonRating.visibility = View.VISIBLE
        }
    }
}