package com.example.kinodata.fragments.movies.movieDetails

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.rate.RateRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
import com.example.kinodata.model.collection.CollectionResult
import com.example.kinodata.model.images.ImageResult
import com.example.kinodata.model.movie.ResultForMovies
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
// SavedStateHandle cannot be used in Shared ViewModel. Thus passing movieId when calling functions
// in MovieDetailsFragment. The pitfall is it calls all these functions everytime Fragment is shown.
// To solve this problem in ViewModel there is a _movieId StateFlow. Fragments observe its value and
// checks if it is equal to current movies' id (SafeArgs.movieId).
// TODO: Check for moviesId in RateFragment?
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // ********************ID**************************
    // to use it in rate func
    private var _movieId = MutableStateFlow(0)
    val movieId = _movieId.asStateFlow()

    fun setMovieId(id: Int) {
        if (_movieId.value != id) {
            _movieId.value = id
            println("SetMovieId to $id")
        }
    }

    // ***********************************UI Part********************************************
    private val _movieDetails = MutableLiveData<NetworkResult<MovieDetails>>(NetworkResult.Loading)
    val movieDetails: LiveData<NetworkResult<MovieDetails>> = _movieDetails

    private val _credits = MutableLiveData<NetworkResult<Credits>>(NetworkResult.Loading)
    val credits: LiveData<NetworkResult<Credits>> = _credits

    private val _reviews = MutableLiveData<NetworkResult<List<Review>>>(NetworkResult.Loading)
    val reviews: LiveData<NetworkResult<List<Review>>> = _reviews

    private val _images = MutableLiveData<NetworkResult<ImageResult>>(NetworkResult.Loading)
    val images: LiveData<NetworkResult<ImageResult>> = _images

    private val _collection = MutableLiveData<NetworkResult<CollectionResult>>(NetworkResult.Loading)
    val collection: LiveData<NetworkResult<CollectionResult>> = _collection

    private val _recommendations = MutableLiveData<NetworkResult<ResultForMovies>>(NetworkResult.Loading)
    val recommendations: LiveData<NetworkResult<ResultForMovies>> = _recommendations

    private val _isFavorite = MutableLiveData<NetworkResult<Boolean>>(NetworkResult.Loading)
    val isFavorite: LiveData<NetworkResult<Boolean>> = _isFavorite

    private val _isInWatchlist = MutableLiveData<NetworkResult<Boolean>>(NetworkResult.Loading)
    val isInWatchlist: LiveData<NetworkResult<Boolean>> = _isInWatchlist

    // current rating by user. If 0 then not rated by user
    private val _ratingByUser = MutableLiveData(.0)
    val ratingByUser: LiveData<Double> = _ratingByUser

    // ***********************************Actions***************************************

    // to show toast msg
    private val _addToFavorite = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val addToFavorite = _addToFavorite.asSharedFlow()

    // to show toast msg
    private val _addToWatchlist = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val addToWatchlist = _addToWatchlist.asSharedFlow()

    // after rating by user it shows toast with the result of rating
    private val _rateResult = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val rate = _rateResult.asSharedFlow()

    // in RateFragment BottomSheetDialog: to show in UI current to rate val. if it is equal to
    // _ratingByUser nothing happens, else updates rating. By default = 7
    private val _toRate = MutableStateFlow(7.0)
    val toRate = _toRate.asStateFlow()

    private val _deleteRating = MutableSharedFlow<NetworkResult<SuccessResponse>>()
    val deleteRating = _deleteRating.asSharedFlow()

    fun getMovieDetails(id: Int) {
        _movieDetails.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(id, MyConstants.LANGUAGE)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _movieDetails.value = NetworkResult.Success(data)
                } else {
                    _movieDetails.value =
                        throwError(mContext.getString(R.string.errorGettingMovieDetails))
                }
            } catch (e: Exception) {
                _movieDetails.value = NetworkResult.Error(e)
            }
        }
    }


    fun getMovieCredits(id: Int) {
        _credits.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMovieCredits(id, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _credits.value = NetworkResult.Success(data)
                    } else {
                        _credits.value =
                            throwError(mContext.getString(R.string.something_went_wrong))
                    }
                } else {
                    _credits.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _credits.value = NetworkResult.Error(e)
            }
        }

    }

    fun getMovieReviews(id: Int) {
        println("movieId in getMovieReviews = $id")
        _reviews.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getMovieReviews(id = id.toString(), language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val data = response.body()?.reviews
                    data?.let {
                        _reviews.value = NetworkResult.Success(it)
                    }
                } else {
                    _reviews.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _reviews.value = NetworkResult.Error(e)
            }
        }

    }

    fun getMovieImages(id: Int) {
        _images.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMovieImages(id)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _images.value = NetworkResult.Success(data)
                } else {
                    _images.value = throwError(mContext.getString(R.string.errorGettingMovieImages))
                }
            } catch (e: Exception) {
                _images.value = NetworkResult.Error(e)
            }
        }
    }

    fun getCollection(id: Int) {
        _collection.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getCollection(id)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _collection.value = NetworkResult.Success(data)
                } else {
                    _collection.value = throwError(
                        mContext.getString(R.string.errorGettingMovieCollection)
                    )
                }
            } catch (e: Exception) {
                _collection.value = NetworkResult.Error(e)
            }
        }
    }

    // not to show collection of prev movie even if doesn't belong to any collection
    fun setCollectionToNull() {
        _collection.value = NetworkResult.Error(Exception("Doesn't belong to any collection"))
    }

    fun getMovieRecommendations(id: Int) {
        _recommendations.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMovieRecommendations(id)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _recommendations.value = NetworkResult.Success(data)
                } else {
                    _recommendations.value = throwError(
                        mContext.getString(R.string.errorGettingMovieRecommendations)
                    )
                }
            } catch (e: Exception) {
                _recommendations.value = NetworkResult.Error(e)
            }
        }
    }

    fun getMovieAccountStates(id: Int) {
        println("movieId in getMovieAccountStates = ${_movieId.value}")
        _ratingByUser.value = .0
        _isFavorite.value = NetworkResult.Loading
        _isInWatchlist.value = NetworkResult.Loading
        viewModelScope.launch {
            _rateResult.emit(NetworkResult.Loading)
            _deleteRating.emit(NetworkResult.Loading)
            try {
                dataStoreRepository.sessionId.collectLatest { sessId ->
                    val response = repository.getMovieAccountStates(id.toString(), sessId)
                    if (response.isSuccessful) {
                        val accountStates = response.body()
                        if (accountStates != null) {
                            // if rated by user it is Rated JsonObject, if not rated then it is Boolean(false)
                            if (accountStates.isRatedItemRatedObject()) {
                                val ratedItem = accountStates.ratedItemAsRatedObject()
                                _ratingByUser.value = ratedItem.value
                            } else {
                                _ratingByUser.value = .0
                            }
                            // Favorite
                            _isFavorite.value = NetworkResult.Success(accountStates.favorite)
                            // Watchlist
                            _isInWatchlist.value = NetworkResult.Success(accountStates.watchlist)
                        } else {
                            _isFavorite.value = throwError(
                                mContext.getString(R.string.something_went_wrong)
                            )
                            _isInWatchlist.value = throwError(
                                mContext.getString(R.string.something_went_wrong)
                            )
                        }
                    } else {
                        _isFavorite.value = throwError(
                            mContext.getString(R.string.something_went_wrong)
                        )
                        _isInWatchlist.value = throwError(
                            mContext.getString(R.string.something_went_wrong)
                        )
                    }
                }
            } catch (e: Exception) {
                _isFavorite.value = NetworkResult.Error(e)
                _isInWatchlist.value = NetworkResult.Error(e)
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
                        val favorite = _isFavorite.value
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
            try {
                dataStoreRepository.accountIdAndSessionId.collectLatest { ids ->
                    val accountId = ids.first
                    val sessionId = ids.second
                    // not empty means user is signed in
                    if (sessionId.isNotEmpty()) {
                        val inWatchlist = _isInWatchlist.value
                        when (inWatchlist) {
                            is NetworkResult.Success -> {
                                val watchlist = inWatchlist.data
                                // if it is in watchlist then remove from list
                                val requestBody = AddToWatchlistRequestBody(
                                    watchlist = !watchlist,
                                    media_id = id,
                                    media_type = MyConstants.MEDIA_TYPE_MOVIE
                                )
                                val response = repository
                                    .addToWatchlist(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToWatchlist.emit(NetworkResult.Success(it))
                                        if (it.success) {
                                            // if it was in Watchlist before, now it is not
                                            _isInWatchlist.value = NetworkResult.Success(!watchlist)
                                        }
                                    }
                                } else {
                                    _addToWatchlist.emit(throwError(mContext.getString(R.string.couldntAddToWatchlist)))
                                }
                            }
                            is NetworkResult.Error -> {
                                _isInWatchlist.value =
                                    throwError(mContext.getString(R.string.couldntAddToWatchlist))
                                _addToWatchlist.emit(throwError(mContext.getString(R.string.couldntAddToWatchlist)))
                            }
                            else -> {
                                _isInWatchlist.value = NetworkResult.Loading
                                _addToWatchlist.emit(NetworkResult.Loading)
                            }
                        }
                    } else {
                        _isInWatchlist.value =
                            throwError(mContext.getString(R.string.please_sign_in))
                        _addToWatchlist.emit(throwError(mContext.getString(R.string.please_sign_in)))
                    }
                }
            } catch (e: Exception) {
                _addToWatchlist.emit(NetworkResult.Error(e))
                _isInWatchlist.value = NetworkResult.Error(e)
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
                    Log.d(TAG, "requestBody val = ${requestBody.value}, movieId = $_movieId")
                    val response =
                        repository.rateMovie(_movieId.value.toString(), sessionId, requestBody)
                    Log.d(TAG, "rate: ${response.code()} + ${response.message()}")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _ratingByUser.value = rating
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

    fun deleteRating() {
        viewModelScope.launch {
            _deleteRating.emit(NetworkResult.Loading)
            try {
                dataStoreRepository.sessionId.collectLatest { sessionId ->
                    val response =
                        repository.deleteMovieRating(_movieId.value.toString(), sessionId)
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            _deleteRating.emit(NetworkResult.Success(data))
                            if (data.success) {
                                _ratingByUser.value = .0
                            }
                        } else {
                            _deleteRating.emit(throwError(mContext.getString(R.string.couldntDeleteRating)))
                        }
                    } else {
                        _deleteRating.emit(throwError(mContext.getString(R.string.couldntDeleteRating)))
                    }
                }
            } catch (e: Exception) {
                _deleteRating.emit(NetworkResult.Error(e))
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