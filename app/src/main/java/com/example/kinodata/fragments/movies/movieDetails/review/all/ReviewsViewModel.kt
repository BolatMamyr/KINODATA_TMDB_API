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

class ReviewsViewModel(private val repository: Repository, private val movieId: String)
    : ViewModel() {

    // TODO: Now its only getting first page (first 12 elements) coz paging leads to infinite duplication. Check this: https://medium.com/nerd-for-tech/pagination-in-android-with-paging-3-retrofit-and-kotlin-flow-2c2454ff776e
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