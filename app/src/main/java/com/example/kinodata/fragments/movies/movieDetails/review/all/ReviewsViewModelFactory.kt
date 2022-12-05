package com.example.kinodata.fragments.movies.movieDetails.review.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class ReviewsViewModelFactory(private val repository: Repository, private val movieId: String)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReviewsViewModel(repository, movieId) as T
    }
}