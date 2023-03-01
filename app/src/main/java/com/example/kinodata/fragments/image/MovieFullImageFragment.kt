package com.example.kinodata.fragments.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kinodata.databinding.FragmentMovieFullImageBinding
import com.example.kinodata.fragments.image.adapters.FullImageAdapter
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.utils.NetworkResult

class MovieFullImageFragment : Fragment() {

    private val args by navArgs<MovieFullImageFragmentArgs>()

    private var _binding: FragmentMovieFullImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MovieDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieFullImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbFullImg.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data.backdrops.map { it.file_path }
                    val mAdapter = FullImageAdapter(data)
                    binding.viewpagerImg.apply {
                        adapter = mAdapter
                        // On predraw so it can change currentItemTime before drawing the view
                        doOnPreDraw {
                            setCurrentItem(args.imageNumber, false)
                        }
                    }
                }
                else -> {}
            }
        }



    }

}