package com.example.kinodata.fragments.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class MoviesFragViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesFragViewModel(repository) as T
    }
}