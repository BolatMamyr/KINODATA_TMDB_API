package com.example.kinodata.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kinodata.databinding.FragmentPersonFullImageBinding
import com.example.kinodata.fragments.image.adapters.FullImageAdapter
import com.example.kinodata.utils.NetworkResult

class PersonFullImageFragment : Fragment() {

    private val args by navArgs<PersonFullImageFragmentArgs>()

    private var _binding: FragmentPersonFullImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<PersonViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonFullImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbPersonFullImg.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data.profiles.map { it.file_path }
                    val mAdapter = FullImageAdapter(data)
                    binding.viewpagerPersonImg.apply {
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