package com.example.kinodata.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentPersonBinding
import com.example.kinodata.repo.Repository

class PersonFragment : Fragment() {

    private val args: PersonFragmentArgs by navArgs()

    private val viewModel: PersonViewModel by viewModels {
        PersonViewModelFactory(Repository(), args.personId.toString())
    }

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPersonInfo()
        viewModel.person.observe(viewLifecycleOwner) {
            binding.tbPerson.title = it.name
            binding.txtPersonName.text = it.name
            if (it.also_known_as.isNotEmpty()) {
                binding.txtPersonAlsoKnownAs.text = it.also_known_as[0]
            }
            binding.txtPersonKnownFor.text = it.known_for_department

            val birthday = getDate(it.birthday)
            val deathday = it.deathday?.let { it1 -> getDate(it1) }
            val date = if (deathday != null) {
                "$birthday - $deathday"
            } else {
                birthday
            }
            binding.txtPersonDate.text = date
            Glide.with(view.context)
                .load(MyConstants.IMG_BASE_URL + it.profile_path)
                .into(binding.imgPerson)
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