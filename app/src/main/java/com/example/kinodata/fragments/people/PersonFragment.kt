package com.example.kinodata.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.adapters.PersonActingMoviesHorizontalAdapter
import com.example.kinodata.adapters.PersonActingTvHorizontalAdapter
import com.example.kinodata.adapters.PersonMoviesAsCrewHorizontalAdapter
import com.example.kinodata.adapters.PersonTvAsCrewHorizontalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentPersonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : Fragment() {
    // TODO: After navigating back to PersonFragment crew member Lists Not Showing anything!!!!!!!!!!!!!!
    // TODO: Crew Members are being repeated if director and producer at the same time.
    private val args: PersonFragmentArgs by navArgs()

    private val viewModel: PersonViewModel by viewModels()

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
        binding.svPerson.isSaveEnabled = true
        binding.tbPerson.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        getPersonInfo()
        getPersonMovies(view)
        getPersonTvSeries(view)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnPersonSeeDetails.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonMoreDetailsFragment(args.personId)
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllActingMovies.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllMoviesFragment(
                    personId = args.personId,
                    category = MyConstants.CATEGORY_ACTING_MOVIES
                )
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllActingTv.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllMoviesFragment(
                    personId = args.personId,
                    category = MyConstants.CATEGORY_ACTING_TV
                )
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllMoviesAsCrew.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllMoviesFragment(
                    personId = args.personId,
                    category = MyConstants.CATEGORY_MOVIES_AS_CREW
                )
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllTvAsCrew.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllMoviesFragment(
                    personId = args.personId,
                    category = MyConstants.CATEGORY_TV_AS_CREW
                )
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllMoviesAsCrew.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllMoviesAsCrewFragment(args.personId)
            findNavController().navigate(action)
        }

        binding.btnPersonSeeAllTvAsCrew.setOnClickListener {
            val action = PersonFragmentDirections
                .actionPersonFragmentToPersonAllTvAsCrewFragment(args.personId)
            findNavController().navigate(action)
        }
    }


    private fun getPersonInfo() {
        viewModel.getPersonInfo()
        viewModel.person.observe(viewLifecycleOwner) {
            binding.tbPerson.title = it.name
            binding.txtPersonName.text = it.name

            val department = if (it.known_for_department == MyConstants.DEPARTMENT_ACTING) {
                if (it.gender == 1) {
                    getString(R.string.Actress)
                } else {
                    getString(R.string.Actor)
                }
            } else {
                getString(R.string.department) + it.known_for_department
            }
            binding.txtPersonKnownFor.text = department


            val birthday = getDate(it.birthday)
            val deathday = it.deathday?.let { it1 -> getDate(it1) }
            val date = if (deathday != null) {
                "$birthday - $deathday"
            } else {
                birthday
            }
            binding.txtPersonDate.text = date

            val profilePath = it.profile_path

            if (profilePath == null) {
                // TODO: Not showing ProfileBlankPic
                binding.imgPerson.setImageResource(R.drawable.profileblankpic)
            } else {
                Glide.with(requireContext())
                    .load(MyConstants.IMG_BASE_URL + profilePath)
                    .into(binding.imgPerson)
            }

        }
    }

    private fun getPersonMovies(view: View) {
        viewModel.getPersonMovieCredits()

        val actingMoviesAdapter = PersonActingMoviesHorizontalAdapter()
        val moviesAsCrewAdapter = PersonMoviesAsCrewHorizontalAdapter()

        binding.rvPersonActingMovies.apply {
            adapter = actingMoviesAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
        }

        binding.rvPersonMoviesAsCrew.apply {
            adapter = moviesAsCrewAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
        }

        viewModel.actingMovies.observe(viewLifecycleOwner) {
            // *********************Acting Movies***************************
            // if no movies hide layout
            if (it.isEmpty()) {
                binding.layoutActingMovies.visibility = View.GONE
            } else {
                binding.layoutActingMovies.visibility = View.VISIBLE
            }

            val sortedList = it.sortedByDescending { it.popularity }.take(20)
            actingMoviesAdapter.updateData(sortedList)
        }

        viewModel.moviesAsCrew.observe(viewLifecycleOwner) {
            // *********************Movies As Crew***************************
            if (it.isEmpty()) {
                binding.layoutMoviesAsCrew.visibility = View.GONE
            } else {
                binding.layoutMoviesAsCrew.visibility = View.VISIBLE
            }
            val crewSortedList = it.sortedByDescending { it.popularity }.take(20)
            moviesAsCrewAdapter.updateData(crewSortedList)
        }

        // Acting Movies RV OnItemClickListener
        actingMoviesAdapter.onItemClick = {
            it?.id?.let { movieId ->
                val action = PersonFragmentDirections
                    .actionPersonFragmentToMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }
        }

        // Movies As Crew RV OnItemClickListener
        moviesAsCrewAdapter.onItemClick = {
            it?.id?.let { movieId ->
                val action = PersonFragmentDirections
                    .actionPersonFragmentToMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }
        }

    }

    private fun getPersonTvSeries(view: View) {
        viewModel.getPersonTvSeriesCredits()

        val actingTvAdapter = PersonActingTvHorizontalAdapter()
        val tvAsCrewAdapter = PersonTvAsCrewHorizontalAdapter()

        binding.rvPersonActingTv.apply {
            adapter = actingTvAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
        }

        binding.rvPersonTvAsCrew.apply {
            adapter = tvAsCrewAdapter
            val manager = LinearLayoutManager(view.context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = manager
        }

        viewModel.actingTv.observe(viewLifecycleOwner) {
            // *****************Acting TV Series*****************************
            if (it.isEmpty()) {
                binding.layoutActingTv.visibility = View.GONE
            } else {
                binding.layoutActingTv.visibility = View.VISIBLE
            }

            val sortedList = it.sortedByDescending { it.popularity }.take(20)
            actingTvAdapter.updateData(sortedList)

        }

        viewModel.tvAsCrew.observe(viewLifecycleOwner) {
            // *****************TV Series As Crew*****************************
            if (it.isEmpty()) {
                binding.layoutTvAsCrew.visibility = View.GONE
            } else {
                binding.layoutTvAsCrew.visibility = View.VISIBLE
            }

            val crewSortedList = it.sortedByDescending { it.popularity }.take(20)
            tvAsCrewAdapter.updateData(crewSortedList)

        }

        // Acting Tv RV OnItemClickListener
        actingTvAdapter.onItemClick = {
            it?.id?.let { tvId ->
                val action = PersonFragmentDirections
                    .actionPersonFragmentToTvDetailsFragment(tvId)
                findNavController().navigate(action)
            }
        }
        // TV As Crew RV OnItemClickListener
        tvAsCrewAdapter.onItemClick = {
            it?.id?.let { tvId ->
                val action = PersonFragmentDirections
                    .actionPersonFragmentToTvDetailsFragment(tvId)
                findNavController().navigate(action)
            }
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