package com.example.kinodata.fragments.tvSeries.tvDetails

import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.review.Review
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "TvDetailsViewModel"

@HiltViewModel
class TvDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
    ) : ViewModel() {

    init {
        Log.d(TAG, "initialized: ")
    }
    private val tvId = state.get<Int>("tvSeriesId").toString()

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
                Log.d(TAG, "getTvDetails: ${e.message}")
            }

        }
    }

    fun getTvCredits() {
        viewModelScope.launch {
            try {
                val response = repository.getTvCredits(tvId, MyConstants.LANGUAGE)
                if(response.isSuccessful) {
                    _credits.value = response.body()
                    Log.d(TAG, "Cast and Crew sizes: ${response.body()?.cast?.size} - ${response.body()?.crew?.size}" )
                }
            } catch (e: Exception) {
                Log.d(TAG, "getTvCredits: ${e.message}")
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
                Log.d(TAG, "Error: getTvReviews: ${e.message}")
            }
        }
    }

}