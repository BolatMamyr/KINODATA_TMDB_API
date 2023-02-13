package com.example.kinodata.fragments.review.all

import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllReviewsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
    ): ViewModel() {

    private val id = state.get<String>("movieId") ?: "null"

    private val _movieReviews: MutableLiveData<List<Review>> = MutableLiveData()
    val movieReviews: LiveData<List<Review>> = _movieReviews

    private val _tvReviews: MutableLiveData<List<Review>> = MutableLiveData()
    val tvReviews: LiveData<List<Review>> = _tvReviews

    fun getMovieReviews() {
        viewModelScope.launch {
            try {
                val response = repository
                        .getMovieReviews(id = id, language = MyConstants.LANGUAGE)

                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let { _movieReviews.value = it }
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getMovieReviews: ${e.message}")
            }
        }
    }

    fun getTvReviews() {
        viewModelScope.launch {
            try {
                val response = repository
                        .getTvReviews(tvId = id, language = MyConstants.LANGUAGE)

                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let { _tvReviews.value = it }
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getTvReviews: ${e.message}")
            }
        }
    }
}