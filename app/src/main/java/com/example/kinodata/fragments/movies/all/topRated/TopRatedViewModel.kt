package com.example.kinodata.fragments.movies.all.topRated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.paging.MoviesPagingSource
import com.example.kinodata.repo.Repository

class TopRatedViewModel(private val repository: Repository) : ViewModel() {

    val movies = Pager(PagingConfig(20)) {
        MoviesPagingSource(context = "Top", repository)
    }.flow.cachedIn(viewModelScope)
}