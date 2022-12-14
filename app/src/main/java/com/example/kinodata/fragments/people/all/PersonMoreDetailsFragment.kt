package com.example.kinodata.fragments.people.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.kinodata.R
import com.example.kinodata.databinding.FragmentPersonMoreDetailsBinding
import com.example.kinodata.fragments.people.PersonViewModel
import com.example.kinodata.fragments.people.PersonViewModelFactory
import com.example.kinodata.repo.Repository

class PersonMoreDetailsFragment : Fragment() {

    private val args: PersonMoreDetailsFragmentArgs by navArgs()

    private val viewModel: PersonViewModel by viewModels {
        PersonViewModelFactory(Repository(), args.personId.toString())
    }

    private var _binding: FragmentPersonMoreDetailsBinding? = null
    private val binding get() = _binding!!

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
        viewModel.getPersonInfo()
        viewModel.person.observe(viewLifecycleOwner) {
            binding.tbPersonMoreDetails.title = it.name
            binding.txtPersonMoreInfoKnownFor.text = it.known_for_department
            binding.txtPersonMoreInfoDateOfBirth.text = getDate(it.birthday)
            if (it.deathday == "null" || it.deathday == null) {
                binding.layoutDateOfDeath.visibility = View.GONE
            } else {
                binding.txtPersonMoreInfoDateOfDeath.text = getDate(it.deathday)
            }
            binding.txtPersonMoreInfoPlaceOfBirth.text = if (it.place_of_birth == "null") {
                "-"
            } else {
                it.place_of_birth
            }
            binding.txtPersonMoreInfoHomepage.text = if (it.homepage == "null") {
                "-"
            } else {
                it.homepage
            }
            binding.txtBiography.text = it.biography
        }
    }

    private fun getDate(date: String): String {
        return if (date.length > 7) {
            val year = date.substring(0, 4)
            var month = date.substring(5, 7)
            var day = date.substring(8)
            if (day[0] == '0') day = day[1].toString()

            month = when (month) {
                "01" -> resources.getString(R.string.january)
                "02" -> resources.getString(R.string.february)
                "03" -> resources.getString(R.string.march)
                "04" -> resources.getString(R.string.april)
                "05" -> resources.getString(R.string.may)
                "06" -> resources.getString(R.string.june)
                "07" -> resources.getString(R.string.july)
                "08" -> resources.getString(R.string.august)
                "09" -> resources.getString(R.string.september)
                "10" -> resources.getString(R.string.october)
                "11" -> resources.getString(R.string.november)
                "12" -> resources.getString(R.string.december)
                else -> ""
            }
            "$day $month, $year"
        } else ""
    }

}