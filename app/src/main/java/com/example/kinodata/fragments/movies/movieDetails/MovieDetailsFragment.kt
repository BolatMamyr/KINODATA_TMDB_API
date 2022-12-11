package com.example.kinodata.fragments.movies.movieDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.adapters.CastHorizontalAdapter
import com.example.kinodata.adapters.ReviewHorizontalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentMovieDetailsBinding
import com.example.kinodata.repo.Repository

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(Repository(), args.movieId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svMovieDetails.isSaveEnabled = true
        // *************************MovieDetails*************************
        viewModel.getMovieDetails()
        val movie = viewModel.movie
        movie.observe(viewLifecycleOwner) {
            binding.tbMovieDetails.title = it.title
            binding.txtDetailsTitle.text = it.title
            binding.txtDetailsVoteAve.text = String.format("%.1f", it.vote_average)
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
            binding.txtDetailsVoteAveBig.text = String.format("%.1f", it.vote_average)
            binding.txtDetailsVoteCountBig.text = it.vote_count.toString()

            Glide.with(view)
                .load(MyConstants.IMG_BASE_URL + it.poster_path)
                .into(binding.imgMovieDetailsPoster)
        }

        // *************************Credits**********************************
        val castHorizontalAdapter = CastHorizontalAdapter()

        binding.rvMovieDetailsCast.apply {
            adapter = castHorizontalAdapter
            val manager = GridLayoutManager(view.context, 4)
            manager.orientation = RecyclerView.HORIZONTAL
            layoutManager = manager
        }
        viewModel.getMovieCredits()
        viewModel.credits.observe(viewLifecycleOwner) {
            val cast = it.cast
            val firstFour = it.getFirstFourActors()
            binding.txtDetailsStars.text =
                "${resources.getString(R.string.stars)} $firstFour ${resources.getString(R.string.and_others)}"
            if(cast.size > 12) {
                castHorizontalAdapter.updateData(cast.take(12))
            } else {
                castHorizontalAdapter.updateData(cast)
            }
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
            movie.value?.id?.toString()?.let { it1 ->
                val action = MovieDetailsFragmentDirections
                    .actionMovieDetailsFragmentToAllReviewsFragment(it1)
                findNavController().navigate(action)
            }
        }
        // TODO: get Reviews(or Firebase to make own review system), similar movies

    }


}