package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentAllPersonImagesBinding
import com.example.kinodata.fragments.image.adapters.AllImagesAdapter
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult

class AllPersonImagesFragment : Fragment() {

    private var _binding: FragmentAllPersonImagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<PersonViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllPersonImagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllPersonImages.setNavigationOnClickListener { findNavController().navigateUp() }

        val mAdapter = AllImagesAdapter()
        binding.rvAllPersonImages.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            isSaveEnabled = true
        }

        mAdapter.onItemClick = {
            it?.let { imgNumber ->
                val action = AllPersonImagesFragmentDirections
                    .actionAllPersonImagesFragmentToPersonFullImageFragment(imgNumber)
                findNavController().navigate(action)
            }
        }
        viewModel.images.observe(viewLifecycleOwner) {
            when (it) {
                NetworkResult.Loading -> binding.pbAllPersonImages.visibility = View.VISIBLE
                is NetworkResult.Success -> {
                    val data = it.data.profiles.map { it.file_path }
                    mAdapter.updateData(data)
                    binding.pbAllPersonImages.visibility = View.GONE
                }
                is NetworkResult.Error -> {
                    toast(getString(R.string.something_went_wrong))
                }
            }
        }
    }

}