package com.example.kinodata.fragments.movies.movieDetails

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.account.accountStates.AccountStates
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MovieDetailsViewModel"

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    state: SavedStateHandle,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // TODO: impl "add to watchlist", "rate" func

    init {
        val id = state.get<Int>("movieId") ?: 0
        getMovieReviews(id)
        getMovieAccountStates(id)
        getMovieCredits(id)
        getMovieDetails(id)
    }

    private var _reviews = MutableStateFlow<NetworkResult<List<Review>>>(NetworkResult.Loading)
    val reviews = _reviews.asStateFlow()

    private val _addToFavorite = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val addToFavorite = _addToFavorite.asSharedFlow()

    private val _addToWatchlist = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val addToWatchlist = _addToWatchlist.asSharedFlow()

    private val _accountState =
        MutableStateFlow<NetworkResult<AccountStates>>(NetworkResult.Loading)
    val accountState = _accountState.asStateFlow()

    private val _movie = MutableStateFlow<NetworkResult<MovieDetails>>(NetworkResult.Loading)
    val movie = _movie.asStateFlow()

    private val _credits = MutableStateFlow<NetworkResult<Credits>>(NetworkResult.Loading)
    val credits = _credits.asStateFlow()

    private fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(id.toString(), MyConstants.LANGUAGE)
                Log.d(TAG, "getMovieDetails response code: ${response.code()}")
                Log.d(TAG, "getMovieDetails movieId: $id")
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        _movie.value = NetworkResult.Success(movie)
                    } else {
                        _movie.value = throwError()
                    }
                } else {
                    _movie.value = throwError()
                }
            } catch (e: Exception) {
                _movie.value = NetworkResult.Error(e)
            }
        }
    }


    private fun getMovieCredits(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovieCredits(id.toString(), MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { _credits.value = NetworkResult.Success(it) }
                } else {
                    _credits.value = throwError()
                }
            } catch (e: Exception) {
                _credits.value = NetworkResult.Error(e)
            }
        }
    }

    private fun getMovieReviews(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getMovieReviews(id = id.toString(), language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()?.reviews
                    data?.let { _reviews.value = NetworkResult.Success(it) }
                } else {
                    _reviews.value = throwError()
                }
            } catch (e: Exception) {
                _reviews.value = NetworkResult.Error(e)
            }
        }
    }

    private fun getMovieAccountStates(id: Int) {
        viewModelScope.launch {
            try {
                dataStoreRepository.sessionId.collectLatest { sessionId ->
                    val response = repository.getMovieAccountStates(id.toString(), sessionId)
                    if (response.isSuccessful) {
                        response.body()?.let { _accountState.value = NetworkResult.Success(it) }
                    } else {
                        _accountState.value = throwError()
                    }
                }
            } catch (e: Exception) {
                _accountState.value = NetworkResult.Error(e)
            }
        }
    }


    fun addToFavorite(id: Int) {
        viewModelScope.launch {
            dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                if (isSignedIn) {
                    // After clicking button check if it is favorite, only then do operation
                    getMovieAccountStates(id)
                    try {
                        val accState = accountState.value
                        // if it can get movie account state then it is signed in
                        if (accState is NetworkResult.Success) {
                            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                                val accountId = pair.first
                                val sessionId = pair.second

                                // if it is already marked as favorite then remove from favorites
                                val isFavorite = accState.data.favorite
                                val requestBody = AddToFavoriteRequestBody(
                                    favorite = !isFavorite,
                                    media_id = id,
                                    media_type = MyConstants.MEDIA_TYPE_MOVIE
                                )
                                val response = repository
                                    .addToFavorite(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToFavorite.emit(NetworkResult.Success(it))
                                        // after changing its favorite state call func again to trigger UI
                                        getMovieAccountStates(id)
                                    }
                                } else {
                                    _addToFavorite.emit(throwError())
                                }
                            }
                        }


                    } catch (e: Exception) {
                        _addToFavorite.emit(NetworkResult.Error(e))
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.please_sign_in), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addToWatchlist(id: Int) {
        viewModelScope.launch {
            dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                if (isSignedIn) {
                    // After clicking button check if it is favorite, only then do operation
                    getMovieAccountStates(id)
                    try {
                        val accState = accountState.value
                        // if it can get movie account state then it is signed in
                        if (accState is NetworkResult.Success) {
                            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                                val accountId = pair.first
                                val sessionId = pair.second

                                // if it is already marked as favorite then remove from favorites
                                val isInWatchlist = accState.data.watchlist
                                val requestBody = AddToWatchlistRequestBody(
                                    watchlist = !isInWatchlist,
                                    media_id = id,
                                    media_type = MyConstants.MEDIA_TYPE_MOVIE
                                )
                                val response = repository
                                    .addToWatchlist(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToWatchlist.emit(NetworkResult.Success(it))
                                        // after changing its favorite state call func again to trigger UI
                                        getMovieAccountStates(id)
                                    }
                                } else {
                                    _addToWatchlist.emit(throwError())
                                }
                            }
                        }


                    } catch (e: Exception) {
                        _addToWatchlist.emit(NetworkResult.Error(e))
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.please_sign_in), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun throwError(): NetworkResult.Error {
        return NetworkResult.Error(
            Exception(mContext.getString(R.string.something_went_wrong))
        )
    }
}