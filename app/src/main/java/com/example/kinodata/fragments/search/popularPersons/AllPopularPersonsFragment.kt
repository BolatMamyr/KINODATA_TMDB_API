package com.example.kinodata.fragments.search.popularPersons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinodata.databinding.FragmentAllPopularPersonsBinding
import com.example.kinodata.fragments.search.SearchViewModel
import com.example.kinodata.fragments.search.adapters.PopularPersonsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllPopularPersonsFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentAllPopularPersonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllPopularPersonsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAllPopularPersons.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val mAdapter = PopularPersonsPagingAdapter()
        binding.rvAllPopularPersons.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )
        }

        lifecycleScope.launch {
            viewModel.popularPersonsPaging.collect {
                mAdapter.submitData(it)
            }
        }

        mAdapter.onItemClick = {
            it?.id?.let { personId ->
                val action = AllPopularPersonsFragmentDirections
                    .actionAllPopularPersonsFragmentToPersonFragment(personId)
                findNavController().navigate(action)
            }
        }
    }

}