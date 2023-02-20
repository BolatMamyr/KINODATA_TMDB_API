package com.example.kinodata.fragments.profile.lists.favorite

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.kinodata.fragments.profile.lists.favorite.movies.FavoriteMoviesPagingSource
import com.example.kinodata.fragments.profile.lists.favorite.tv.FavoriteTvPagingSource
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FavoriteViewModel"

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _favoriteMovies: MutableLiveData<Pager<Int, RMovie>> = MutableLiveData()
    val favoriteMovies: LiveData<Pager<Int, RMovie>> = _favoriteMovies

    private val _favoriteTv: MutableLiveData<Pager<Int, RTvSeries>> = MutableLiveData()
    val favoriteTv: LiveData<Pager<Int, RTvSeries>> = _favoriteTv

    fun getFavoriteMovies() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                val sessionId = pair.second
                val movies = Pager(PagingConfig(pageSize = 20)) {
                    FavoriteMoviesPagingSource(repository, accountId, sessionId)
                }
                _favoriteMovies.value = movies
            }
        }
    }

    fun getFavoriteTv() {
        viewModelScope.launch {
            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                val accountId = pair.first
                val sessionId = pair.second
                val tv = Pager(PagingConfig(pageSize = 20)) {
                    FavoriteTvPagingSource(repository, accountId, sessionId)
                }
                _favoriteTv.value = tv
            }
        }
    }

}