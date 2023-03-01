package com.example.kinodata.fragments.image

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentAllMovieImagesBinding
import com.example.kinodata.fragments.image.adapters.AllImagesAdapter
import com.example.kinodata.fragments.movies.movieDetails.MovieDetailsViewModel
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult

private const val TAG = "AllMovieImagesFragment"
class AllMovieImagesFragment : Fragment() {

    private var _binding: FragmentAllMovieImagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MovieDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMovieImagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllMovieImages.setNavigationOnClickListener { findNavController().navigateUp() }

        val mAdapter = AllImagesAdapter()
        binding.rvAllMovieImages.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            isSaveEnabled = true
        }

        mAdapter.onItemClick = {
            it?.let { imgNumber ->
                val action = AllMovieImagesFragmentDirections
                    .actionAllMovieImagesFragmentToMovieFullImageFragment(imgNumber)
                findNavController().navigate(action)
            }
        }
        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                NetworkResult.Loading -> binding.pbAllMovieImages.visibility = View.VISIBLE
                is NetworkResult.Success -> {
                    val data = it.data.backdrops.map { it.file_path }
                    mAdapter.updateData(data)
                    binding.pbAllMovieImages.visibility = View.GONE
                }
                is NetworkResult.Error -> {
                    toast(getString(R.string.something_went_wrong))
                }
            }
        }
    }

}