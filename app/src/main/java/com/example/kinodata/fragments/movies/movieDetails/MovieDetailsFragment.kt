package com.example.kinodata.fragments.movies.movieDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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
import com.example.kinodata.databinding.FragmentMovieDetailsBinding
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.collectLatestLifecycleFlow
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: launch separate coroutines for each UI related stuff. Now it is loading slow

private const val TAG = "MovieDetailsFragment"

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by activityViewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    // argument of RateFragment to rate movie
    private var posterPath = ""

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbMovieDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.svMovieDetails.isSaveEnabled = true
        getMovieDetails(view)
        getMovieCredits(view)
        getReviews()
        getAccountStates()
        collectIsFavorite()
        addToFavorite()
        addToWatchlist()
        rateMovie()
        collectRateResult()
        collectRatingByUser()
    }

    private fun collectIsFavorite() {
        collectLatestLifecycleFlow(viewModel.isFavorite) {
            when(it) {
                is NetworkResult.Success -> {
                    val isFavorite = it.data
                    val img = if (isFavorite) {
                        R.drawable.ic_favorite_red
                    } else {
                        R.drawable.ic_favorite_gray
                    }
                    binding.imgDetailsFavorite.setImageResource(img)
                    binding.imgDetailsFavorite.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    binding.imgDetailsFavorite.setImageResource(R.drawable.ic_favorite_gray)
                    binding.imgDetailsFavorite.visibility = View.VISIBLE
                }
                else -> {
                    binding.imgDetailsFavorite.visibility = View.GONE
                }
            }
        }
    }

    private fun collectRatingByUser() {
        collectLatestLifecycleFlow(viewModel.ratingByUser) {
            when (it) {
                is NetworkResult.Success -> {
                    val rating = it.data
                    Log.d(TAG, "collectRatingByUser: $rating")
                    if (rating > 0) {
                        showRatingByUserUi()
                        val colorId = MyUtils.getRatingColorId(rating, requireView())
                        binding.cardRatingByUser.setCardBackgroundColor(colorId)
                        binding.txtRatingByUser.text = rating.toString()
                    } else {
                        hideRatingByUserUi()
                    }
                }
                else -> {
                    hideRatingByUserUi()
                }

            }

        }
    }

    // after rating by user it shows toast with the result of rating
    private fun collectRateResult() {
        lifecycleScope.launch {
            viewModel.rate.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data
                        if (response.success) {
                            toast(getString(R.string.movie_rated_successfully))
                        } else {
                            toast(getString(R.string.couldntRateMovie))
                        }
                    }
                    is NetworkResult.Error -> {
                        toast(getString(R.string.couldntRateMovie))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun rateMovie() {
        binding.btnDetailsRate.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                    if (!isSignedIn) {
                        toast(getString(R.string.please_sign_in))
                    } else {
                        val action = MovieDetailsFragmentDirections
                            .actionMovieDetailsFragmentToRateFragment()
                        findNavController().navigate(action)
                    }
                }
            }

        }
    }

    private fun addToFavorite() {
        binding.imgDetailsFavorite.setOnClickListener {
            viewModel.addToFavorite(args.movieId)
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
        binding.imgDetailsWatchLater.setOnClickListener {
            viewModel.addToWatchlist(args.movieId)
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

    private fun getAccountStates() {
        viewModel.getMovieAccountStates(args.movieId)
        collectLatestLifecycleFlow(viewModel.accountState) {
            when (it) {
                is NetworkResult.Success -> {
                    val accountStates = it.data
                    val isInWatchlist = accountStates.watchlist

                    // Watchlist
                    val imgWatchlist = if (isInWatchlist) {
                        R.drawable.ic_watch_later_orange
                    } else {
                        R.drawable.ic_watch_later_gray
                    }
                    binding.imgDetailsWatchLater.setImageResource(imgWatchlist)

                }
                else -> {

                }
            }
        }
    }

    private fun getReviews() {
        viewModel.getMovieReviews(args.movieId)
        val reviewHorizontalAdapter = ReviewHorizontalAdapter()
        reviewHorizontalAdapter.onItemClick = {
            it?.let { review ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToReviewFragment(review)
                findNavController().navigate(action)
            }
        }

        binding.rvMovieDetailsReviews.apply {
            this.adapter = reviewHorizontalAdapter
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
            isSaveEnabled = true
        }
        collectLatestLifecycleFlow(viewModel.reviews) {
            when (it) {
                is NetworkResult.Success -> {
                    reviewHorizontalAdapter.updateData(it.data)
                }
                else -> {}
            }
        }

        // Click Listener for See All Reviews button
        binding.btnDetailsSeeAllReviews.setOnClickListener {
            val action = MovieDetailsFragmentDirections
                .actionMovieDetailsFragmentToAllReviewsFragment(
                    movieId = args.movieId.toString(), context = MyConstants.MOVIE
                )
            findNavController().navigate(action)
        }
    }


    private fun getMovieCredits(
        view: View
    ) {
        viewModel.getMovieCredits(args.movieId)
        // ***********Cast************
        val castHorizontalAdapter = CastHorizontalAdapter()
        val crewHorizontalAdapter = CrewHorizontalAdapter()

        binding.rvMovieDetailsCast.apply {
            adapter = castHorizontalAdapter
            val manager = GridLayoutManager(view.context, 4)
            manager.orientation = RecyclerView.HORIZONTAL
            layoutManager = manager
        }
        binding.rvMovieDetailsCrew.apply {
            adapter = crewHorizontalAdapter
            layoutManager = LinearLayoutManager(
                view.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        collectLatestLifecycleFlow(viewModel.credits) {
            when (it) {
                is NetworkResult.Success -> {
                    val credits = it.data
                    val cast = credits.cast
                    val crew = credits.crew

                    val firstFour = credits.getFirstFourActors()

                    if (credits.cast.isNotEmpty()) {
                        binding.txtDetailsStars.text = if (credits.cast.size < 4) {
                            "${resources.getString(R.string.stars)} $firstFour"
                        } else {
                            "${resources.getString(R.string.stars)} $firstFour ${
                                resources.getString(
                                    R.string.and_others
                                )
                            }"
                        }
                    }

                    castHorizontalAdapter.updateData(cast.take(12))

                    // TODO: if crew member is more popular than director it still should be after director. PUT DIRECTOR FIRST SOMEHOW OR SEPARATE FIELD FOR HIM
                    val sortedList = crew.sortedByDescending { it.popularity }
                    crewHorizontalAdapter.updateData(sortedList.take(7))
                }
                else -> {}
            }
        }

        // Click Listener for See All Cast button
        binding.btnDetailsSeeAllCast.setOnClickListener {
            val action = MovieDetailsFragmentDirections
                .actionMovieDetailsFragmentToAllMovieCastFragment(args.movieId)
            findNavController().navigate(action)
        }

        // Click Listener for cast RV item
        castHorizontalAdapter.onItemClick = {
            it?.id?.let { id ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToPersonFragment(id)
                findNavController().navigate(action)
            }
        }

        // Click Listener for See All Crew button
        binding.btnDetailsSeeAllCrew.setOnClickListener {
            val action = MovieDetailsFragmentDirections
                .actionMovieDetailsFragmentToAllMovieCrewFragment(args.movieId)
            findNavController().navigate(action)

        }

        // Click Listener for crew RV item
        crewHorizontalAdapter.onItemClick = {
            it?.id?.let { id ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToPersonFragment(id)
                // isCastMember variable is needed to get filmography in PersonFragment.kt
                findNavController().navigate(action)
            }
        }
    }

    private fun getMovieDetails(view: View) {
        viewModel.getMovieDetails(args.movieId)
        collectLatestLifecycleFlow(viewModel.movie) {
            when (it) {
                is NetworkResult.Success -> {
                    val movie = it.data
                    posterPath = movie.poster_path

                    binding.tbMovieDetails.title = movie.title
                    binding.txtDetailsTitle.text = movie.title

                    val rating = movie.vote_average
                    binding.txtDetailsVoteAve.text = String.format("%.1f", rating)

                    val colorId = MyUtils.getRatingColorId(rating, view)
                    binding.txtDetailsVoteAve.setTextColor(colorId)

                    val voteCount = if (movie.vote_count < 1000) {
                        movie.vote_count.toString()
                    } else {
                        "${(movie.vote_count / 1000)}K"
                    }
                    binding.txtDetailsVoteCount.text = voteCount
                    binding.txtDetailsTitleOriginal.text = movie.original_title
                    // TODO: trailing commas are wrong if there is only 1 item
                    binding.txtDetailsYear.text = movie.release_date.take(4) + ","
                    binding.txtDetailsGenres.text = movie.getGenres()
                    binding.txtDetailsCountry.text = movie.getCountries() + ","
                    binding.txtDetailsDescription.text = movie.overview
                    binding.txtDetailsVoteAveBig.text = String.format("%.1f", rating)
                    binding.txtDetailsVoteAveBig.setTextColor(colorId)

                    binding.txtDetailsVoteCountBig.text = movie.vote_count.toString()

                    Glide.with(view)
                        .load(MyConstants.IMG_BASE_URL + movie.poster_path)
                        .into(binding.imgMovieDetailsPoster)
                    showUi()
                }
                is NetworkResult.Error -> {
                    // TODO: if error show reload page button
                    hideUi()
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideUi()
                }
            }

        }


    }

    private fun showUi() {
        binding.apply {
            pbMovieDetails.visibility = View.GONE
            svMovieDetails.visibility = View.VISIBLE
        }
    }

    private fun hideUi() {
        binding.apply {
            pbMovieDetails.visibility = View.VISIBLE
            svMovieDetails.visibility = View.GONE
        }
    }

    private fun hideRatingByUserUi() {
        binding.apply {
            cardRatingByUser.visibility = View.GONE

        }
    }

    private fun showRatingByUserUi() {
        binding.apply {
            cardRatingByUser.visibility = View.VISIBLE
        }
    }

}