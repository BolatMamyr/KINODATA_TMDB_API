package com.example.kinodata.fragments.movies.all.verticalList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.fragments.movies.adapters.MoviesPagingSource
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerticalListViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
    ) : ViewModel() {

    private val category = state.get<String>("category") ?: "null"

    val movies = Pager(PagingConfig(pageSize = 20)) {
        MoviesPagingSource(category, repository)
    }.flow.cachedIn(viewModelScope)

}