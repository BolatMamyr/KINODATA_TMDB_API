package com.example.kinodata.fragments.movies.movieDetails.review.all

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.review.Review
import com.example.kinodata.paging.ReviewsPagingSource
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class ReviewsViewModel(private val repository: Repository, private val movieId: String)
    : ViewModel() {

    private val _reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val reviews: LiveData<List<Review>> = _reviews

    fun getReviews() {
        viewModelScope.launch {
            try {
                val response = repository.getReviews(movieId, MyConstants.LANGUAGE, "1")
                if (response.isSuccessful) {
                    _reviews.value = response.body()?.reviews
                }
            } catch (e: Exception) {
                Log.d("MyLog", "ReviewsViewModel: getReviews Error: ${e.message}")
            }
        }
    }
}