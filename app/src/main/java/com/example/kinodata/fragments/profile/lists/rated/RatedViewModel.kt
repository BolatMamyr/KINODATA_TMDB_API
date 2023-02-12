package com.example.kinodata.fragments.profile.lists.rated

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kinodata.fragments.profile.lists.rated.movies.RatedMoviesPagingSource
import com.example.kinodata.fragments.profile.lists.rated.tv.RatedTvPagingSource
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RatedViewModel"

@HiltViewModel
class RatedViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _ratedMovies: MutableLiveData<Pager<Int, RMovie>> = MutableLiveData()
    val ratedMovies: LiveData<Pager<Int, RMovie>> = _ratedMovies

    private val _ratedTv: MutableLiveData<Pager<Int, RTvSeries>> = MutableLiveData()
    val ratedTv: LiveData<Pager<Int, RTvSeries>> = _ratedTv

    fun getRatedMovies() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                Log.d(TAG, "getRatedMovies: accountId = $accountId")
                val sessionId = pair.second
                val movies = Pager(PagingConfig(pageSize = 20)) {
                    RatedMoviesPagingSource(repository, accountId, sessionId)
                }
                _ratedMovies.value = movies
            }
        }
    }

    fun getRatedTv() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                val sessionId = pair.second
                val tv = Pager(PagingConfig(pageSize = 20)) {
                    RatedTvPagingSource(repository, accountId, sessionId)
                }
                _ratedTv.value = tv
            }
        }
    }


}