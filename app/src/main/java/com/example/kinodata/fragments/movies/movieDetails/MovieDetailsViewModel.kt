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
import com.example.kinodata.model.account.rate.RateRequestBody
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

// Shared ViewModel across: MovieDetailsFragment and RateFragment
// SavedStateHandle cannot be used in Shared ViewModel
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private lateinit var sessionId: String

    init {
        viewModelScope.launch {
            dataStoreRepository.sessionId.collectLatest {
                sessionId = it
            }
        }
    }

    // will initialize it in getMovieDetails func to access it in rate func
    private var movieId = 0

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

    // current rating by user. If 0 then not rated by user
    private val _ratingByUser = MutableStateFlow(.0)
    val ratingByUser = _ratingByUser.asStateFlow()

    // after rating by user it shows toast with the result of rating
    private val _rateResult = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val rate = _rateResult.asSharedFlow()

    // in RateFragment BottomSheetDialog: to show in UI current to rate val. if it is equal to
    // _ratingByUser nothing happens, else updates rating. By default = 7
    private val _toRate = MutableStateFlow(7.0)
    val toRate = _toRate.asStateFlow()

    fun getMovieDetails(id: Int) {
        movieId = id
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(id.toString(), MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        _movie.value = NetworkResult.Success(movie)
                    } else {
                        _movie.value = throwError(mContext.getString(R.string.something_went_wrong))
                    }
                } else {
                    _movie.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _movie.value = NetworkResult.Error(e)
            }
        }
    }


    fun getMovieCredits(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovieCredits(id.toString(), MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { _credits.value = NetworkResult.Success(it) }
                } else {
                    _credits.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _credits.value = NetworkResult.Error(e)
            }
        }
    }

    fun getMovieReviews(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getMovieReviews(id = id.toString(), language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()?.reviews
                    data?.let { _reviews.value = NetworkResult.Success(it) }
                } else {
                    _reviews.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _reviews.value = NetworkResult.Error(e)
            }
        }
    }

    fun getMovieAccountStates(id: Int) {
        viewModelScope.launch {
            try {
                dataStoreRepository.sessionId.collectLatest { sessId ->
                    val response = repository.getMovieAccountStates(id.toString(), sessId)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _accountState.value = NetworkResult.Success(it)
                            // if rated by user it is Rated JsonObject, if not rated then it is Boolean(false)
                            if (it.isRatedItemRatedObject()) {
                                val ratedItem = it.ratedItemAsRatedObject()
                                _ratingByUser.emit(ratedItem.value)
                            }
                        }
                    } else {
                        _accountState.value =
                            throwError(mContext.getString(R.string.couldntGetAccountStates))
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
                                    _addToFavorite.emit(throwError(mContext.getString(R.string.couldntMarkAsFavorite)))
                                }
                            }
                        }


                    } catch (e: Exception) {
                        _addToFavorite.emit(NetworkResult.Error(e))
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.please_sign_in),
                        Toast.LENGTH_SHORT
                    ).show()
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
                                    _addToWatchlist.emit(throwError(mContext.getString(R.string.couldntAddToWatchlist)))
                                }
                            }
                        }


                    } catch (e: Exception) {
                        _addToWatchlist.emit(NetworkResult.Error(e))
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.please_sign_in),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun incrementRating() {
        // cannot be rated more than 10
        if (_toRate.value < 10) {
            val n = _toRate.value + .5
            viewModelScope.launch {
                _toRate.emit(n)
            }
        }
    }

    fun subtractRating() {
        // cannot be rated less than 0.5
        if (_toRate.value > .5) {
            val n = _toRate.value - .5
            viewModelScope.launch {
                _toRate.emit(n)
            }
        }
    }

    fun rate(rating: Double) {
        viewModelScope.launch {
            try {
                val requestBody = RateRequestBody(rating)
                Log.d(TAG, "requestBody val = ${requestBody.value}, movieId = $movieId")
                val response = repository.rateMovie(movieId.toString(), sessionId, requestBody)
                Log.d(TAG, "rate: ${response.code()} + ${response.message()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        _ratingByUser.emit(rating)
                        _rateResult.emit(NetworkResult.Success(it))
                    }
                } else {
                    _rateResult.emit(throwError(mContext.getString(R.string.couldntRateMovie)))
                }
            } catch (e: Exception) {
                _rateResult.emit(throwError(mContext.getString(R.string.couldntRateMovie)))
            }
        }
    }

    fun changeToRateValue(rating: Double) {
        viewModelScope.launch {
            _toRate.emit(rating)
        }
    }

    private fun throwError(message: String): NetworkResult.Error {
        return NetworkResult.Error(
            Exception(message)
        )
    }
}