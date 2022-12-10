package com.example.kinodata.fragments.movies.all.verticalList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class VerticalListViewModelFactory(
    private val repository: Repository, private val category: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VerticalListViewModel(repository, category) as T
    }
}