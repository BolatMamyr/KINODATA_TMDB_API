package com.example.kinodata.fragments.tvSeries.tvDetails

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.rate.RateRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
import com.example.kinodata.model.images.ImageResult
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.review.Review
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "TvDetailsViewModel"

@HiltViewModel
class TvDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    state: SavedStateHandle,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // ********************ID**************************
    // to use it in rate func
    private var _tvId = MutableStateFlow(0)
    val tvId = _tvId.asStateFlow()

    fun setTvId(id: Int) {
        if (_tvId.value != id) {
            _tvId.value = id
            println("SetMovieId to $id")
        }
    }

    // ***********************************UI Part********************************************
    private val _tvDetails = MutableLiveData<NetworkResult<TvDetails>>(NetworkResult.Loading)
    val tvDetails: LiveData<NetworkResult<TvDetails>> = _tvDetails

    private val _credits = MutableLiveData<NetworkResult<Credits>>(NetworkResult.Loading)
    val credits: LiveData<NetworkResult<Credits>> = _credits

    private val _reviews = MutableLiveData<NetworkResult<List<Review>>>(NetworkResult.Loading)
    val reviews: LiveData<NetworkResult<List<Review>>> = _reviews

    private val _images = MutableLiveData<NetworkResult<ImageResult>>(NetworkResult.Loading)
    val images: LiveData<NetworkResult<ImageResult>> = _images

    private val _isFavorite = MutableLiveData<NetworkResult<Boolean>>(NetworkResult.Loading)
    val isFavorite: LiveData<NetworkResult<Boolean>> = _isFavorite

    private val _isInWatchlist = MutableLiveData<NetworkResult<Boolean>>(NetworkResult.Loading)
    val isInWatchlist: LiveData<NetworkResult<Boolean>> = _isInWatchlist

    // current rating by user. If 0 then not rated by user
    private val _ratingByUser = MutableLiveData(.0)
    val ratingByUser:LiveData<Double> = _ratingByUser

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

    fun getTvDetails(id: Int) {
        _tvDetails.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getTvDetails(id.toString(), MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val tv = response.body()
                    if (tv != null) {
                        _tvDetails.value = NetworkResult.Success(tv)
                    } else {
                        _tvDetails.value =
                            throwError(mContext.getString(R.string.something_went_wrong))
                    }
                } else {
                    _tvDetails.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _tvDetails.value = NetworkResult.Error(e)
            }
        }
    }

    fun getTvCredits(id: String) {
        _credits.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getTvCredits(id, MyConstants.LANGUAGE)
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

    fun getTvReviews(id: String) {
        _reviews.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getTvReviews(tvId = id, language = MyConstants.LANGUAGE)
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

    fun getTvImages(id: Int) {
        _images.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getTvImages(id)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _images.value = NetworkResult.Success(data)
                } else {
                    _images.value = throwError(mContext.getString(R.string.errorGettingTvImages))
                }
            } catch (e: Exception) {
                _images.value = NetworkResult.Error(e)
            }
        }
    }

    fun getTvAccountStates(id: Int) {
        _ratingByUser.value = .0
        _isFavorite.value = NetworkResult.Loading
        _isInWatchlist.value = NetworkResult.Loading
        viewModelScope.launch {
            _rateResult.emit(NetworkResult.Loading)
            _deleteRating.emit(NetworkResult.Loading)
            try {
                dataStoreRepository.sessionId.collectLatest { sessId ->
                    Log.d(TAG, "sessionId = $sessId")
                    val response = repository.getTvAccountStates(id, sessId)
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
                                    media_type = MyConstants.MEDIA_TYPE_TV
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
                                    media_type = MyConstants.MEDIA_TYPE_TV
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
                    val response = repository.rateTv(_tvId.value.toString(), sessionId, requestBody)
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
                    val response = repository.deleteTvRating(_tvId.value.toString(), sessionId)
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