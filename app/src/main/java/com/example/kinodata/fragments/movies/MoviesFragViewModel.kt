package com.example.kinodata.fragments.movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.paging.MoviesPagingSource
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.MyUtils.Companion.throwError
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesFragViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository
    ) : ViewModel() {

    private val _popularMovies = MutableLiveData<NetworkResult<ResultForMovies>>(NetworkResult.Loading)
    val popularMovies: LiveData<NetworkResult<ResultForMovies>> = _popularMovies

    private val _topMovies = MutableLiveData<NetworkResult<ResultForMovies>>(NetworkResult.Loading)
    val topMovies: LiveData<NetworkResult<ResultForMovies>> = _topMovies

    private val _nowPlayingMovies = MutableLiveData<NetworkResult<ResultForMovies>>(NetworkResult.Loading)
    val nowPlayingMovies: LiveData<NetworkResult<ResultForMovies>> = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<NetworkResult<ResultForMovies>>(NetworkResult.Loading)
    val upcomingMovies: LiveData<NetworkResult<ResultForMovies>> = _upcomingMovies

    private val language = mContext.getString(R.string.language)

    fun getPopularMovies() {
        _popularMovies.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(language, 1)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _popularMovies.value = NetworkResult.Success(data)
                } else {
                    _popularMovies.value = throwError(
                        mContext.getString(R.string.errorGettingPopularMovies)
                    )
                }
            } catch (e: Exception) {
                _popularMovies.value = NetworkResult.Error(e)
            }
        }
    }

    fun getTopRatedMovies() {
        _topMovies.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getTopRatedMovies(language, 1)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _topMovies.value = NetworkResult.Success(data)
                } else {
                    _topMovies.value = throwError(
                        mContext.getString(R.string.errorGettingTopMovies)
                    )
                }
            } catch (e: Exception) {
                _topMovies.value = NetworkResult.Error(e)
            }
        }
    }

    fun getNowPlayingMovies() {
        _nowPlayingMovies.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(language, 1)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _nowPlayingMovies.value = NetworkResult.Success(data)
                } else {
                    _nowPlayingMovies.value = throwError(
                        mContext.getString(R.string.errorGettingNowPlayingMovies)
                    )
                }
            } catch (e: Exception) {
                _nowPlayingMovies.value = NetworkResult.Error(e)
            }
        }
    }

    fun getUpcomingMovies() {
        _upcomingMovies.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUpcomingMovies(language, 1)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _upcomingMovies.value = NetworkResult.Success(data)
                } else {
                    _upcomingMovies.value = throwError(
                        mContext.getString(R.string.errorGettingUpcomingMovies)
                    )
                }
            } catch (e: Exception) {
                _upcomingMovies.value = NetworkResult.Error(e)
            }
        }
    }



    // TODO: Change all to flow. Also in PagingSource needs to be changed to collect flows to be able to reload

//    fun getPopularMovies() {
//        viewModelScope.launch {
//            try {
//                repository.getPopularMovies(language, 1).retryWhen { cause, attempt ->
//                    if (attempt > 3) {
//                        // Maximum number of retries reached, propagate the error
//                        throw cause
//                    } else {
//                        // Delay before retrying
//                        delay(1000L)
//                        // Retry the network call
//                        true
//                    }
//                }.collect { response ->
//                    val data = response.body()
//                    if (response.isSuccessful && data != null) {
//                        _popularMovies.value = NetworkResult.Success(data)
//                    } else {
//                        _popularMovies.value = throwError(
//                            mContext.getString(R.string.errorGettingPopularMovies)
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _popularMovies.value = NetworkResult.Error(e)
//            }
//        }
//    }

}