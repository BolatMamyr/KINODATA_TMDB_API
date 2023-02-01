package com.example.kinodata.fragments.profile.lists.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.kinodata.fragments.profile.lists.watchlist.movies.MoviesWatchlistPagingSource
import com.example.kinodata.fragments.profile.lists.watchlist.tv.TvWatchlistPagingSource
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _movies: MutableLiveData<Pager<Int, RMovie>> = MutableLiveData()
    val movies: LiveData<Pager<Int, RMovie>> = _movies

    private val _tv: MutableLiveData<Pager<Int, RTvSeries>> = MutableLiveData()
    val tv: LiveData<Pager<Int, RTvSeries>> = _tv

    fun getMoviesWatchlist() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                val sessionId = pair.second
                val movies = Pager(PagingConfig(pageSize = 20)) {
                    MoviesWatchlistPagingSource(repository, accountId, sessionId)
                }
                _movies.value = movies
            }
        }
    }

    fun getTvWatchlist() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                val sessionId = pair.second
                val tv = Pager(PagingConfig(pageSize = 20)) {
                    TvWatchlistPagingSource(repository, accountId, sessionId)
                }
                _tv.value = tv
            }
        }
    }


}