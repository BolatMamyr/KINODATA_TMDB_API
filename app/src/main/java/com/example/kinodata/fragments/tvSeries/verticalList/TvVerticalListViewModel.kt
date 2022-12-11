package com.example.kinodata.fragments.tvSeries.verticalList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.paging.TvPagingSource
import com.example.kinodata.repo.Repository

class TvVerticalListViewModel(
    private val repository: Repository, private val category: String
    ) : ViewModel() {

    val tvSeries = Pager(PagingConfig(pageSize = 20)) {
        TvPagingSource(repository, category)
    }.flow.cachedIn(viewModelScope)
}