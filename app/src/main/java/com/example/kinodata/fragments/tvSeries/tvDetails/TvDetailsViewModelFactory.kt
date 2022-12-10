package com.example.kinodata.fragments.tvSeries.tvDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinodata.repo.Repository

@Suppress("UNCHECKED_CAST")
class TvDetailsViewModelFactory(private val repository: Repository, private val tvId: String)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TvDetailsViewModel(repository, tvId) as T
    }
}