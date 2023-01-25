package com.example.kinodata.fragments.movies.movieDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

//private val Context.datastore: DataStore<Preferences> by dataStore()
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

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
        val movieDetails = getMovieDetails(view)
        getMovieCredits(view, movieDetails)
        getReviews(movieDetails)

        // TODO: finish adding to favs. Always observe if favorite and depending on it change UI state
        viewModel.markAsFavoriteNetworkState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkState.Success -> {
                    binding.imgDetailsFavorite.setImageResource(R.drawable.ic_star_orange)
                    Toast.makeText(
                        context,
                        getString(R.string.addedToFavorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkState.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.couldntMarkAsFavorite),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
        binding.imgDetailsFavorite.setOnClickListener {
                viewModel.markAsFavorite()
            }

    }

    private fun getReviews(movieDetails: MovieDetails?) {
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
        viewModel.getMovieReviews()
        viewModel.reviews.observe(viewLifecycleOwner) {
            it?.let { list -> reviewHorizontalAdapter.updateData(list) }
        }
        // Click Listener for See All Reviews button
        binding.btnDetailsSeeAllReviews.setOnClickListener {
            movieDetails?.id?.toString()?.let { id ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToAllReviewsFragment(
                        movieId = id, context = MyConstants.MOVIE
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun getMovieCredits(
        view: View,
        movieDetails: MovieDetails?
    ) {
        viewModel.getMovieCredits()

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

        viewModel.credits.observe(viewLifecycleOwner) {
            val cast = it.cast
            val crew = it.crew

            val firstFour = it.getFirstFourActors()

            if (it.cast.isNotEmpty()) {
                if (it.cast.size < 4) {
                    binding.txtDetailsStars.text =
                        "${resources.getString(R.string.stars)} $firstFour"
                } else {
                    binding.txtDetailsStars.text =
                        "${resources.getString(R.string.stars)} $firstFour ${resources.getString(R.string.and_others)}"
                }
            }

            castHorizontalAdapter.updateData(cast.take(12))

            // TODO: if crew member is more popular than director it still should be after director. PUT DIRECTOR FIRST SOMEHOW OR SEPARATE FIELD FOR HIM
            val sortedList = crew.sortedByDescending { it.popularity }
            crewHorizontalAdapter.updateData(sortedList.take(7))
        }
        // Click Listener for See All Cast button
        binding.btnDetailsSeeAllCast.setOnClickListener {
            movieDetails?.id?.let {
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToAllMovieCastFragment(it)
                findNavController().navigate(action)
            }
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
            movieDetails?.id?.let {
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToAllMovieCrewFragment(it)
                findNavController().navigate(action)
            }
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

    private fun getMovieDetails(view: View): MovieDetails? {
        viewModel.getMovieDetails()
        val movieDetails = viewModel.movie
        movieDetails.observe(viewLifecycleOwner) {
            binding.tbMovieDetails.title = it.title
            binding.txtDetailsTitle.text = it.title

            val rating = it.vote_average
            binding.txtDetailsVoteAve.text = String.format("%.1f", rating)

            val colorId = MyUtils.getRatingColorId(rating, view)
            binding.txtDetailsVoteAve.setTextColor(colorId)

            val voteCount = if (it.vote_count < 1000) {
                it.vote_count.toString()
            } else {
                "${(it.vote_count / 1000)}K"
            }
            binding.txtDetailsVoteCount.text = voteCount
            binding.txtDetailsTitleOriginal.text = it.original_title
            binding.txtDetailsYear.text = it.release_date.take(4) + ","
            binding.txtDetailsGenres.text = it.getGenres()
            binding.txtDetailsCountry.text = it.getCountries() + ","
            binding.txtDetailsDescription.text = it.overview
            binding.txtDetailsVoteAveBig.text = String.format("%.1f", rating)
            binding.txtDetailsVoteAveBig.setTextColor(colorId)

            binding.txtDetailsVoteCountBig.text = it.vote_count.toString()

            Glide.with(view)
                .load(MyConstants.IMG_BASE_URL + it.poster_path)
                .into(binding.imgMovieDetailsPoster)
        }
        return movieDetails.value
    }

    private fun setClickListeners() {

    }

}