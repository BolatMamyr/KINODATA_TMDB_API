package com.example.kinodata.fragments.tvSeries.tvDetails.seasonDetails

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.R
import com.example.kinodata.model.tv.season.SeasonDetails
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.MyUtils.Companion.throwError
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SeasonDetailsViewModel"

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository
) : ViewModel() {

    private val _seasonDetails = MutableLiveData<NetworkResult<SeasonDetails>>(NetworkResult.Loading)
    val seasonDetails: LiveData<NetworkResult<SeasonDetails>> = _seasonDetails

    fun getSeasonDetails(id: Int, seasonNumber: Int) {
        _seasonDetails.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.getSeasonDetails(id, seasonNumber)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _seasonDetails.value = NetworkResult.Success(data)
                } else {
                    _seasonDetails.value = throwError(
                        mContext.getString(R.string.errorGettingSeasonDetails)
                    )
                }
            } catch (e: Exception) {
                _seasonDetails.value = NetworkResult.Error(e)
            }
        }
    }
}