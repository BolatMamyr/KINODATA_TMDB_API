package com.example.kinodata.fragments.tvSeries.tvDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.credit.Credits
import com.example.kinodata.model.review.Review
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class TvDetailsViewModel(private val repository: Repository, private val tvId: String) : ViewModel() {

    private var _tvSeries: MutableLiveData<TvDetails> = MutableLiveData()
    val tvSeries: LiveData<TvDetails> = _tvSeries

    private var _credits: MutableLiveData<Credits> = MutableLiveData()
    val credits: LiveData<Credits> = _credits

    private var _reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val reviews: LiveData<List<Review>> = _reviews

    fun getTvDetails() {
        viewModelScope.launch {
            try {
                val response = repository.getTvDetails(tvId, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    _tvSeries.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("MyLog", "getTvDetails: ${e.message}")
            }

        }
    }

    fun getTvCredits() {
        viewModelScope.launch {
            try {
                val response = repository.getTvCredits(tvId, MyConstants.LANGUAGE)
                if(response.isSuccessful) {
                    _credits.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("MyLog", "getTvCredits: ${e.message}")
            }
        }
    }

    fun getTvReviews() {
        viewModelScope.launch {
            try {
                val response = repository
                    .getTvReviews(tvId = tvId, language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let {
                        _reviews.value = it
                    }
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getTvReviews: ${e.message}")
            }
        }
    }

}