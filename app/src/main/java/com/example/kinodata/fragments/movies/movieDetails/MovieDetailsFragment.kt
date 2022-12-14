package com.example.kinodata.fragments.movies.movieDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.kinodata.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

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

//        val dataStoreRepository = DataStoreRepository(view.context)
//        lifecycleScope.launch {
//            dataStoreRepository.readFromDataStore.collect {
//                Log.d(TAG, "onViewCreated: sessionId = $it")
//            }
//        }

        binding.tbMovieDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.svMovieDetails.isSaveEnabled = true
        // *************************MovieDetails*************************
        viewModel.getMovieDetails()
        val movie = viewModel.movie
        movie.observe(viewLifecycleOwner) {
            binding.tbMovieDetails.title = it.title
            binding.txtDetailsTitle.text = it.title

            val rating = it.vote_average
            binding.txtDetailsVoteAve.text = String.format("%.1f", rating)

            val colorId = MyUtils.getRatingColorId(rating, view)
            binding.txtDetailsVoteAve.setTextColor(colorId)

            val voteCount = if(it.vote_count < 1000) {
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

        // *************************Credits**********************************

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
                    binding.txtDetailsStars.text = "${resources.getString(R.string.stars)} $firstFour"
                } else {
                    binding.txtDetailsStars.text = "${resources.getString(R.string.stars)} $firstFour ${resources.getString(R.string.and_others)}"
                }
            }

            castHorizontalAdapter.updateData(cast.take(12))

            // TODO: if crew member is more popular than director it still should be after director. PUT DIRECTOR FIRST SOMEHOW OR SEPARATE FIELD FOR HIM
            val sortedList = crew.sortedByDescending { it.popularity }
            crewHorizontalAdapter.updateData(sortedList.take(7))
        }
        // Click Listener for See All Cast button
        binding.btnDetailsSeeAllCast.setOnClickListener {
            movie.value?.id?.let {
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
            movie.value?.id?.let {
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

        // **************************Reviews***********************************
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
            movie.value?.id?.toString()?.let { id ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToAllReviewsFragment(
                        movieId = id, context = MyConstants.MOVIE
                    )
                findNavController().navigate(action)
            }
        }

    }


}