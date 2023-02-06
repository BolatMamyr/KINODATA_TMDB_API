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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MovieDetailsViewModel"

// TODO: add isInWatchlist StateFlow for UI(see isFavorite). Also make 3 dots(see more) btn depend on it.
// Shared ViewModel across: MovieDetailsFragment and RateFragment
// SavedStateHandle cannot be used in Shared ViewModel
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    init {
        Log.d(TAG, "INITIALIZED")
    }

    // will initialize it in getMovieDetails func to access it in rate func
    private var movieId = 0

    private var _reviews = MutableStateFlow<NetworkResult<List<Review>>>(NetworkResult.Loading)
    val reviews = _reviews.asStateFlow()

    // to show toast msg
    private val _addToFavorite = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val addToFavorite = _addToFavorite.asSharedFlow()
    // for UI State
    private val _isFavorite = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Loading)
    val isFavorite = _isFavorite.asStateFlow()

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
    private val _ratingByUser = MutableStateFlow<NetworkResult<Double>>(NetworkResult.Loading)
    val ratingByUser = _ratingByUser.asStateFlow()

    // after rating by user it shows toast with the result of rating
    private val _rateResult = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val rate = _rateResult.asSharedFlow()

    // in RateFragment BottomSheetDialog: to show in UI current to rate val. if it is equal to
    // _ratingByUser nothing happens, else updates rating. By default = 7
    private val _toRate = MutableStateFlow(7.0)
    val toRate = _toRate.asStateFlow()

    fun getMovieDetails(id: Int) {
        _movie.value = NetworkResult.Loading
        // Since SavedStateHandle does not work in SharedViewModel save movieId to use it later
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
        _credits.value = NetworkResult.Loading
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
        _reviews.value = NetworkResult.Loading
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
        _accountState.value = NetworkResult.Loading
        _ratingByUser.value = NetworkResult.Loading
        _isFavorite.value = NetworkResult.Loading
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
                                _ratingByUser.value = NetworkResult.Success(ratedItem.value)
                            } else {
                                _ratingByUser.value = throwError("Not rated by user")
                            }
                            // Favorite
                            _isFavorite.value = NetworkResult.Success(it.favorite)
                        }
                    } else {
                        _accountState.value =
                            throwError(mContext.getString(R.string.couldntGetAccountStates))
                        _isFavorite.value = throwError(
                            mContext.getString(R.string.something_went_wrong)
                        )
                    }
                }
            } catch (e: Exception) {
                _accountState.value = NetworkResult.Error(e)
                _isFavorite.value = NetworkResult.Error(e)
            }
        }
    }


    fun addToFavorite(id: Int) {
        viewModelScope.launch {
            try {
                dataStoreRepository.accountIdAndSessionId.collectLatest { ids ->
                    val accountId = ids.first
                    val sessionId = ids.second
                    // not empty means user is signed in
                    if (sessionId.isNotEmpty()) {
                        val favorite = isFavorite.value
                        when (favorite) {
                            is NetworkResult.Success -> {
                                val fav = favorite.data
                                // if it is already marked as favorite then remove from favorites
                                val requestBody = AddToFavoriteRequestBody(
                                    favorite = !fav,
                                    media_id = id,
                                    media_type = MyConstants.MEDIA_TYPE_MOVIE
                                )
                                val response = repository
                                    .addToFavorite(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToFavorite.emit(NetworkResult.Success(it))
                                        if (it.success) {
                                            // if it was favorite before now it is not
                                            _isFavorite.value = NetworkResult.Success(!fav)
                                        }
                                        // after changing its favorite state call func again to trigger UI
                                    }
                                } else {
                                    _addToFavorite.emit(throwError(mContext.getString(R.string.couldntMarkAsFavorite)))
                                }
                            }
                            is NetworkResult.Error -> {
                                _isFavorite.value =
                                    throwError(mContext.getString(R.string.couldntMarkAsFavorite))
                                _addToFavorite.emit(throwError(mContext.getString(R.string.couldntMarkAsFavorite)))
                            }
                            else -> {
                                _isFavorite.value = NetworkResult.Loading
                                _addToFavorite.emit(NetworkResult.Loading)
                            }
                        }
                    } else {
                        _isFavorite.value = throwError(mContext.getString(R.string.please_sign_in))
                        _addToFavorite.emit(throwError(mContext.getString(R.string.please_sign_in)))
                    }
                }
            } catch (e: Exception) {
                _addToFavorite.emit(NetworkResult.Error(e))
                _isFavorite.value = NetworkResult.Error(e)
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
            _rateResult.emit(NetworkResult.Loading)
            try {
                dataStoreRepository.sessionId.collectLatest { sessionId ->
                    val requestBody = RateRequestBody(rating)
                    Log.d(TAG, "requestBody val = ${requestBody.value}, movieId = $movieId")
                    val response = repository.rateMovie(movieId.toString(), sessionId, requestBody)
                    Log.d(TAG, "rate: ${response.code()} + ${response.message()}")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _ratingByUser.value = NetworkResult.Success(rating)
                            _rateResult.emit(NetworkResult.Success(it))
                        }
                    } else {
                        _rateResult.emit(throwError(mContext.getString(R.string.couldntRateMovie)))
                    }
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