package com.example.kinodata.fragments.movies.movieDetails.review.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class AllReviewsViewModelFactory(
    private val repository: Repository, private val context: String, private val id: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllReviewsViewModel(repository, context, id) as T
    }
}