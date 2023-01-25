package com.example.kinodata.fragments.movies.movieDetails

import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.favorite.MarkAsFavoriteRequestBody
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MovieDetailsViewModel"
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
    ) : ViewModel() {

    private val id = state.get<Int>("movieId") ?: 0
    private var _movie: MutableLiveData<MovieDetails> = MutableLiveData()
    val movie: LiveData<MovieDetails> = _movie

    private var _credits: MutableLiveData<Credits> = MutableLiveData()
    val credits: LiveData<Credits> = _credits

    private var _reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val reviews: LiveData<List<Review>> = _reviews

    private val _markAsFavorite: MutableLiveData<SuccessResponse> = MutableLiveData()
    val markAsFavorite: LiveData<SuccessResponse> = _markAsFavorite

    private val _markAsFavoriteNetworkState: MutableLiveData<NetworkState> = MutableLiveData()
    val markAsFavoriteNetworkState: LiveData<NetworkState> = _markAsFavoriteNetworkState

    fun getMovieDetails() {
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(id.toString(), MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    _movie.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("MyLog", "getMovieDetails: ${e.message}")
            }

        }
    }

    fun getMovieCredits() {
        viewModelScope.launch {
            try {
                val response = repository.getMovieCredits(id.toString(), MyConstants.LANGUAGE)
                if(response.isSuccessful) {
                    _credits.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("MyLog", "getMovieCredits: ${e.message}")
            }
        }
    }

    fun getMovieReviews() {
        viewModelScope.launch {
            try {
                val response = repository
                    .getMovieReviews(id = id.toString(), language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let { _reviews.value = it }
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getReviews: ${e.message}")
            }
        }
    }

    fun markAsFavorite() {
        viewModelScope.launch {
            try {
                dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                    val accountId = pair.first
                    val sessionId = pair.second
                    val requestBody = MarkAsFavoriteRequestBody(
                        favorite = true,
                        media_id = id,
                        media_type = MyConstants.MEDIA_TYPE_MOVIE
                    )
                    val response = repository.markAsFavorite(accountId, sessionId, requestBody)
                    if (response.isSuccessful) {
                        _markAsFavorite.value = response.body()
                        if (response.body()?.success == true) {
                            val message = response.body()?.status_message ?: "null"
                            _markAsFavoriteNetworkState.value = NetworkState.Success(message)
                        } else {
                            _markAsFavoriteNetworkState.value = NetworkState.Error("Couldn't mark as favorite")
                        }

                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "markAsFavorite: ${e.message}")
            }

        }
    }

}