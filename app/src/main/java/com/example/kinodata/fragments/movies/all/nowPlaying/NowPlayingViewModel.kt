package com.example.kinodata.fragments.movies.all.nowPlaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.paging.MoviesPagingSource
import com.example.kinodata.repo.Repository

class NowPlayingViewModel(private val repository: Repository) : ViewModel() {

    val movies = Pager(PagingConfig(pageSize = 20)) {
        MoviesPagingSource("Now", repository)
    }.flow.cachedIn(viewModelScope)
}