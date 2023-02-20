package com.example.kinodata.fragments.tvSeries

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.tv.ResultForTv
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Wrap everything with NetworkResult sealed class.
@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository
    ) : ViewModel() {

    private val _popularTv: MutableLiveData<NetworkResult<ResultForTv>> = MutableLiveData()
    val popularTv: LiveData<NetworkResult<ResultForTv>> = _popularTv

    private val _topTv: MutableLiveData<NetworkResult<ResultForTv>> = MutableLiveData()
    val topTv: LiveData<NetworkResult<ResultForTv>> = _topTv

    private val _airingTv: MutableLiveData<NetworkResult<ResultForTv>> = MutableLiveData()
    val airingTv: LiveData<NetworkResult<ResultForTv>> = _airingTv

    fun getPopularTv() {
        _popularTv.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getPopularTv(mContext.getString(R.string.language), MyConstants.FIRST_PAGE)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _popularTv.value = NetworkResult.Success(data)
                } else {
                    _popularTv.value = MyUtils.throwError(
                        mContext.getString(R.string.errorGettingPopularTv)
                    )
                }
            } catch (e: Exception) {
                _popularTv.value = NetworkResult.Error(e)
            }
        }
    }

    fun getTopTv() {
        _topTv.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getTopTv(mContext.getString(R.string.language), MyConstants.FIRST_PAGE)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _topTv.value = NetworkResult.Success(data)
                } else {
                    _topTv.value = MyUtils.throwError(
                        mContext.getString(R.string.errorGettingTopRatedTv)
                    )
                }
            } catch (e: Exception) {
                _topTv.value = NetworkResult.Error(e)
            }
        }
    }

    fun getAiringTv() {
        _airingTv.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getAiringTv(mContext.getString(R.string.language), MyConstants.FIRST_PAGE)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _airingTv.value = NetworkResult.Success(data)
                } else {
                    _airingTv.value = MyUtils.throwError(
                        mContext.getString(R.string.errorGettingAiringTv)
                    )
                }
            } catch (e: Exception) {
                _airingTv.value = NetworkResult.Error(e)
            }
        }
    }


}