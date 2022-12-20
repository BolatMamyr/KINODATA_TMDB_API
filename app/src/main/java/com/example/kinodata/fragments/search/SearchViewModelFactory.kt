package com.example.kinodata.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val repository: Repository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}