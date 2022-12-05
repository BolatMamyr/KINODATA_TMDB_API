package com.example.kinodata.fragments.movies.all.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.paging.MoviesPagingSource
import com.example.kinodata.repo.Repository

class PopularViewModel(private val repository: Repository) : ViewModel() {
    // TODO: PlaceHolder Not Showing
    // TODO: Stars: if empty shows and others, Countries: If only one country puts comma (France,) ex: Wakanda (2018)
    val movies = Pager(PagingConfig(pageSize = 20)) {
        MoviesPagingSource(context = "Popular", repository)
    }.flow.cachedIn(viewModelScope)

}