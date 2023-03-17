package com.example.kinodata.fragments.tvSeries.tvDetails.seasonDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentSeasonDetailsBinding
import com.example.kinodata.fragments.tvSeries.tvDetails.TvDetailsViewModel
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.toast
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonDetailsFragment : Fragment() {

    private var _binding: FragmentSeasonDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SeasonDetailsViewModel>()
    private val args by navArgs<SeasonDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeasonDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val toolbarTitle = "${args.tvName}: ${args.seasonName}"
            tbSeasonDetails.title = toolbarTitle
            tbSeasonDetails.setNavigationOnClickListener { findNavController().navigateUp() }

            viewModel.getSeasonDetails(args.tvId, args.seasonNumber)
            viewModel.seasonDetails.observe(viewLifecycleOwner) {
                when(it) {
                    NetworkResult.Loading -> {
                        pbSeasonDetails.visibility = View.VISIBLE
                    }
                    is NetworkResult.Success -> {
                        val data = it.data
                        txtSeasonName.text = data.name
                        txtSeasonEpisodeCount.text = data.episodes.size.toString()
                        txtSeasonAirDate.text = MyUtils.getFormattedDate(data.air_date, view)

                        Glide.with(requireContext())
                            .load(MyConstants.IMG_BASE_URL + data.poster_path)
                            .into(imgSeasonDetails)
                        imgSeasonDetails.setOnClickListener {
                            // TODO: open full img
                        }

                        if (data.episodes.isEmpty()) {
                            txtNoEpisodes.visibility = View.VISIBLE
                        } else {
                            txtNoEpisodes.visibility = View.GONE

                            val mAdapter = EpisodeAdapter(data.episodes)
                            rvSeasonDetails.apply {
                                adapter = mAdapter
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    RecyclerView.VERTICAL,
                                    false
                                )
                                isSaveEnabled = true
                                isNestedScrollingEnabled = true
                            }
                        }

                        pbSeasonDetails.visibility = View.GONE
                    }
                    is NetworkResult.Error -> {
                        pbSeasonDetails.visibility = View.GONE
                        toast(getString(R.string.errorGettingSeasonDetails))
                    }
                }
            }
        }

    }

}