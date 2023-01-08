package com.example.kinodata.fragments.movies.movieDetails

import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
    ) : ViewModel() {

    private val id = state.get<Int>("movieId")?.toString() ?: "null"
    private var _movie: MutableLiveData<MovieDetails> = MutableLiveData()
    val movie: LiveData<MovieDetails> = _movie

    private var _credits: MutableLiveData<Credits> = MutableLiveData()
    val credits: LiveData<Credits> = _credits

    private var _reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val reviews: LiveData<List<Review>> = _reviews

    fun getMovieDetails() {
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(id, MyConstants.LANGUAGE)
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
                val response = repository.getMovieCredits(id, MyConstants.LANGUAGE)
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
                    .getMovieReviews(id = id, language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let { _reviews.value = it }
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getReviews: ${e.message}")
            }
        }
    }

}