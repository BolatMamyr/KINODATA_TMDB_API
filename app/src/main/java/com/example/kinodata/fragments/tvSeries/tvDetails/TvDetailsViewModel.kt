package com.example.kinodata.fragments.tvSeries.tvDetails

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.account.accountStates.AccountStates
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
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

    init {
        val id = state.get<Int>("tvSeriesId")
        id?.let { getTvAccountStates(it) }
        getTvCredits(id.toString())
        getTvReviews(id.toString())
        getTvDetails(id.toString())
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

    private val _tvDetails = MutableStateFlow<NetworkResult<TvDetails>>(NetworkResult.Loading)
    val tvDetails = _tvDetails.asStateFlow()

    private val _credits = MutableStateFlow<NetworkResult<Credits>>(NetworkResult.Loading)
    val credits = _credits.asStateFlow()

    private fun getTvDetails(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTvDetails(id, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    response.body()?.let { _tvDetails.value = NetworkResult.Success(it) }
                } else {
                    _tvDetails.value = throwError(mContext.getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                _tvDetails.value = throwError(mContext.getString(R.string.something_went_wrong))
            }

        }
    }

    fun getTvCredits(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTvCredits(id, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    response.body()?.let { _credits.value = NetworkResult.Success(it) }
                } else {
                    _credits.value = throwError(mContext.getString(R.string.couldntGetCredits))
                }
            } catch (e: Exception) {
                _credits.value = NetworkResult.Error(e)
            }
        }
    }

    private fun getTvReviews(id: String) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getTvReviews(tvId = id, language = MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    val list = response.body()?.reviews
                    list?.let {
                        _reviews.value = NetworkResult.Success(it)
                    }
                } else {
                    _reviews.value = throwError(mContext.getString(R.string.couldntGetReviews))
                }
            } catch (e: Exception) {
                _reviews.value = throwError(mContext.getString(R.string.couldntGetReviews))
            }
        }
    }

    private fun getTvAccountStates(id: Int) {
        viewModelScope.launch {
            try {
                dataStoreRepository.sessionId.collectLatest { sessionId ->
                    val response = repository.getTvAccountStates(id, sessionId)
                    if (response.isSuccessful) {
                        response.body()?.let { _accountState.value = NetworkResult.Success(it) }
                    } else {
                        _accountState.value = throwError(
                            mContext.getString(R.string.something_went_wrong)
                        )
                    }
                }
            } catch (e: Exception) {
                _accountState.value = NetworkResult.Error(e)
            }
        }
    }

    fun addOrRemoveFromFavorite(id: Int) {
        viewModelScope.launch {
            dataStoreRepository.isSignedIn.collectLatest { isSignedIn ->
                if (isSignedIn) {
                    // After clicking button check if it is favorite, only then do operation
                    getTvAccountStates(id)
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
                                    media_type = MyConstants.MEDIA_TYPE_TV
                                )
                                val response = repository
                                    .addToFavorite(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToFavorite.emit(NetworkResult.Success(it))
                                        // after changing its favorite state call func again to trigger UI
                                        getTvAccountStates(id)
                                    }
                                } else {
                                    _addToFavorite.emit(
                                        throwError(mContext.getString(R.string.something_went_wrong))
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        _addToFavorite.emit(NetworkResult.Error(e))
                    }
                } else {
                    Toast.makeText(
                        mContext, mContext.getString(R.string.please_sign_in), Toast.LENGTH_SHORT
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
                    getTvAccountStates(id)
                    try {
                        val accState = accountState.value
                        // if it can get movie account state then it is signed in
                        if (accState is NetworkResult.Success) {
                            dataStoreRepository.accountIdAndSessionId.collectLatest { pair ->
                                val accountId = pair.first
                                val sessionId = pair.second

                                // if it is already marked then remove from watchlist
                                val isInWatchlist = accState.data.watchlist
                                val requestBody = AddToWatchlistRequestBody(
                                    watchlist = !isInWatchlist,
                                    media_id = id,
                                    media_type = MyConstants.MEDIA_TYPE_TV
                                )
                                val response = repository
                                    .addToWatchlist(accountId, sessionId, requestBody)
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _addToWatchlist.emit(NetworkResult.Success(it))
                                        // after changing its favorite state call func again to trigger UI
                                        getTvAccountStates(id)
                                    }
                                } else {
                                    _addToWatchlist.emit(
                                        throwError(mContext.getString(R.string.something_went_wrong))
                                    )
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

    private fun throwError(message: String): NetworkResult.Error {
        return NetworkResult.Error(
            Exception(message)
        )
    }


}