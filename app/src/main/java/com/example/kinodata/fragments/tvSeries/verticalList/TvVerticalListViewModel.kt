package com.example.kinodata.fragments.tvSeries.verticalList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.paging.TvPagingSource
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvVerticalListViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val category = state.get<String>("category") ?: "null"

    val tvSeries = Pager(PagingConfig(pageSize = 20)) {
        TvPagingSource(category, repository)
    }.flow.cachedIn(viewModelScope)

}