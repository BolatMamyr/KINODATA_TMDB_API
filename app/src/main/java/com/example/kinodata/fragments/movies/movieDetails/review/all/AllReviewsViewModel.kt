package com.example.kinodata.fragments.movies.movieDetails.review.all

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class AllReviewsViewModel(
    private val repository: Repository, private val context: String, private val id: String
    ): ViewModel() {

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