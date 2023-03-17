package com.example.kinodata.fragments.image

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kinodata.databinding.FragmentTvFullImageBinding
import com.example.kinodata.fragments.image.adapters.FullImageAdapter
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.utils.NetworkResult

private const val TAG = "TvFullImageFragment"
class TvFullImageFragment : Fragment() {


    private val args by navArgs<TvFullImageFragmentArgs>()

    private var _binding: FragmentTvFullImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<TvDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvFullImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbTvFullImg.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data.backdrops.map { it.file_path }
                    val mAdapter = FullImageAdapter(data)
                    binding.viewpagerTvImg.apply {
                        adapter = mAdapter
                        // On preDraw so it can change currentItemTime before drawing the view
                        doOnPreDraw {
                            setCurrentItem(args.imageNumber, false)
                        }
                    }
                }
                is NetworkResult.Loading -> {

                }
                else -> {}
            }
        }



    }


}