package com.example.kinodata.fragments.tvSeries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _popularTvSeries: MutableLiveData<List<RTvSeries>> = MutableLiveData()
    val popularTvSeries: LiveData<List<RTvSeries>> get() = _popularTvSeries

    private val _topRatedTvSeries: MutableLiveData<List<RTvSeries>> = MutableLiveData()
    val topRatedTvSeries: LiveData<List<RTvSeries>> get() = _topRatedTvSeries

    private val _airingTvSeries: MutableLiveData<List<RTvSeries>> = MutableLiveData()
    val airingTvSeries: LiveData<List<RTvSeries>> get() = _airingTvSeries

    fun getPopularTvSeries(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getPopularTvSeries(language, page)
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    list?.let { _popularTvSeries.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getTopRatedTvSeries(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getTopRatedTvSeries(language, page)
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    list?.let { _topRatedTvSeries.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getAiringTvSeries(language: String, page: String) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getAiringTvSeries(language, page)
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    list?.let { _airingTvSeries.value = it }
                }
            } catch (_: Exception) {

            }
        }
    }
}