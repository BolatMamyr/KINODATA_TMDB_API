package com.example.kinodata.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.fragments.people.adaptersHorizontal.PersonActingMoviesHorizontalAdapter
import com.example.kinodata.fragments.people.adaptersHorizontal.PersonActingTvHorizontalAdapter
import com.example.kinodata.fragments.people.adaptersHorizontal.PersonMoviesAsCrewHorizontalAdapter
import com.example.kinodata.fragments.people.adaptersHorizontal.PersonTvAsCrewHorizontalAdapter
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.FragmentPersonBinding
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PersonFragment"

@AndroidEntryPoint
class PersonFragment : Fragment() {
    // TODO: After navigating back to PersonFragment crew member Lists Not Showing anything!!!!!!!!!!!!!!
    // TODO: Crew Members are being repeated if director and producer at the same time.
    private val args: PersonFragmentArgs by navArgs()

    private val viewModel: PersonViewModel by activityViewModels()

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

        if (args.personId != viewModel.personId.value) {
            viewModel.getPersonInfo(args.personId)
            viewModel.getPersonMovieCredits(args.personId)
            viewModel.getPersonTvSeriesCredits(args.personId)
            viewModel.setPersonId(args.personId)
        }

        observePersonInfo()
        observePersonMovies(view)
        observePersonTvSeries(view)

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


    private fun observePersonInfo() {
        viewModel.person.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val person = it.data
                    binding.tbPerson.title = person.name
                    binding.txtPersonName.text = person.name

                    val department = if (person.known_for_department == MyConstants.DEPARTMENT_ACTING) {
                        if (person.gender == 1) {
                            getString(R.string.Actress)
                        } else {
                            getString(R.string.Actor)
                        }
                    } else {
                        getString(R.string.department) + person.known_for_department
                    }
                    binding.txtPersonKnownFor.text = department


                    val birthday = MyUtils.getFormattedDate(person.birthday, requireView())
                    val deathday = person.deathday?.let { deathday ->
                        MyUtils.getFormattedDate(deathday, requireView())
                    }
                    val date = if (deathday != null) {
                        "$birthday - $deathday"
                    } else {
                        birthday
                    }
                    binding.txtPersonDate.text = date

                    val profilePath = person.profile_path

                    if (profilePath == null) {
                        // TODO: Not showing ProfileBlankPic
                        binding.imgPerson.setImageResource(R.drawable.profileblankpic)
                    } else {
                        Glide.with(requireContext())
                            .load(MyConstants.IMG_BASE_URL + profilePath)
                            .into(binding.imgPerson)
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun observePersonMovies(view: View) {
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

        viewModel.movies.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    // TODO: add progressBar
                }
                is NetworkResult.Success -> {
                    val cast = it.data.cast
                    val crew = it.data.crew

                    if (cast.isNotEmpty()) {
                        val sortedList = cast.sortedByDescending { it.popularity }.take(20)
                        actingMoviesAdapter.updateData(sortedList)
                        binding.layoutActingMovies.visibility = View.VISIBLE
                    } else {
                        binding.layoutActingMovies.visibility = View.GONE
                    }
                    if (crew.isNotEmpty()) {
                        val sortedList = crew.sortedByDescending { it.popularity }.take(20)
                        moviesAsCrewAdapter.updateData(sortedList)
                        binding.layoutMoviesAsCrew.visibility = View.VISIBLE
                    } else {
                        binding.layoutMoviesAsCrew.visibility = View.GONE
                    }
                }
                is NetworkResult.Error -> {
                    binding.layoutActingMovies.visibility = View.GONE
                    binding.layoutMoviesAsCrew.visibility = View.GONE
                }
            }
        }
    }

    private fun observePersonTvSeries(view: View) {
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

        viewModel.tv.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    // TODO: add progressBar
                }
                is NetworkResult.Success -> {
                    val cast = it.data.cast
                    val crew = it.data.crew

                    if (cast.isNotEmpty()) {
                        val sortedList = cast.sortedByDescending { it.popularity }.take(20)
                        actingTvAdapter.updateData(sortedList)
                        binding.layoutActingTv.visibility = View.VISIBLE
                    } else {
                        binding.layoutActingTv.visibility = View.GONE
                    }
                    if (crew.isNotEmpty()) {
                        val sortedList = crew.sortedByDescending { it.popularity }.take(20)
                        tvAsCrewAdapter.updateData(sortedList)
                        binding.layoutTvAsCrew.visibility = View.VISIBLE
                    } else {
                        binding.layoutTvAsCrew.visibility = View.GONE
                    }
                }
                is NetworkResult.Error -> {
                    binding.layoutActingTv.visibility = View.GONE
                    binding.layoutTvAsCrew.visibility = View.GONE
                }
            }
        }
    }

}