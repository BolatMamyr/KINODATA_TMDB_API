package com.example.kinodata.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentPersonMoreDetailsBinding
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonMoreDetailsFragment : Fragment() {

    private val viewModel: PersonViewModel by activityViewModels()

    private var _binding: FragmentPersonMoreDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: PersonMoreDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonMoreDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbPersonMoreDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.svPersonMoreDetails.isSaveEnabled = true

        if (args.personId != viewModel.personId.value) {
            viewModel.getPersonInfo(args.personId)
            viewModel.getPersonMovieCredits(args.personId)
            viewModel.getPersonTvSeriesCredits(args.personId)
            viewModel.setPersonId(args.personId)
        }

        viewModel.person.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data
                    binding.tbPersonMoreDetails.title = data.name
                    binding.txtPersonMoreInfoKnownFor.text = data.known_for_department
                    binding.txtPersonMoreInfoDateOfBirth.text =
                        MyUtils.getFormattedDate(data.birthday, requireView())
                    if (!(data.deathday == "null" || data.deathday == null)) {
                        binding.layoutDateOfDeath.visibility = View.VISIBLE
                        binding.txtPersonMoreInfoDateOfDeath.text =
                            MyUtils.getFormattedDate(data.deathday, requireView())
                    }
                    binding.txtPersonMoreInfoPlaceOfBirth.text = if (data.place_of_birth == "null") {
                        "-"
                    } else {
                        data.place_of_birth
                    }
                    binding.txtPersonMoreInfoHomepage.text = if (data.homepage == "null") {
                        "-"
                    } else {
                        data.homepage
                    }
                    binding.txtBiography.text = data.biography
                }
                else -> {

                }
            }
        }

    }
}