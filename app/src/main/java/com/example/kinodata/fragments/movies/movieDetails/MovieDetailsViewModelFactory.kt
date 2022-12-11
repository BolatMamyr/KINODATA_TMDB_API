package com.example.kinodata.fragments.movies.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class MovieDetailsViewModelFactory(
    private val repository: Repository, private val id: String
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(repository, id = id) as T
    }
}