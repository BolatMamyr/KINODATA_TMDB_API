package com.example.kinodata.fragments.tvSeries.verticalList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class TvVerticalListViewModelFactory(
    private val repository: Repository, private val category: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TvVerticalListViewModel(repository, category) as T
    }
}