package com.example.kinodata.fragments.movies.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class MoviesFragViewModel(private val repository: Repository) : ViewModel() {
    private val _popularMovies: MutableLiveData<List<RMovie>> = MutableLiveData()
    val popularMovies: LiveData<List<RMovie>> = _popularMovies

    private val _topMovies: MutableLiveData<List<RMovie>> = MutableLiveData()
    val topMovies: LiveData<List<RMovie>> = _topMovies

    private val _nowPlayingMovies: MutableLiveData<List<RMovie>> = MutableLiveData()
    val nowPlayingMovies: LiveData<List<RMovie>> = _nowPlayingMovies

    private val _upcomingMovies: MutableLiveData<List<RMovie>> = MutableLiveData()
    val upcomingMovies: LiveData<List<RMovie>> = _upcomingMovies

    fun getPopularMovies(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(language, page)
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = result?.results
                    list?.let { _popularMovies.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getTopRatedMovies(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopRatedMovies(language, page)
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = result?.results
                    list?.let { _topMovies.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getNowPlayingMovies(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(language, page)
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = result?.results
                    list?.let { _nowPlayingMovies.value = it }
                }
            } catch (_: Exception) {

            }

        }
    }

    fun getUpcomingMovies(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUpcomingMovies(language, page)
                if (response.isSuccessful) {
                    val result = response.body()
                    val list = result?.results
                    list?.let { _upcomingMovies.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }

}