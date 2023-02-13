package com.example.kinodata.fragments.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.R
import com.example.kinodata.model.account.accountDetails.AccountDetails
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
import com.example.kinodata.model.auth.requestBodies.ValidateTokenRequestBody
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.NetworkResult
import com.example.kinodata.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    //for Sign in layout
    private val _signInNetworkState: MutableLiveData<NetworkState> = MutableLiveData()
    val signInNetworkState: LiveData<NetworkState> = _signInNetworkState

    //for Profile info layout
    private val _accountDetails: MutableLiveData<NetworkResult<AccountDetails>> = MutableLiveData()
    val accountDetails: LiveData<NetworkResult<AccountDetails>> = _accountDetails

    private val _signOutNetworkState: MutableLiveData<NetworkState> = MutableLiveData()
    val signOutNetworkState: LiveData<NetworkState> = _signOutNetworkState


    //***************************************Sign in*********************************
    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            try {
                _signInNetworkState.value = NetworkState.Loading
                val requestToken = getRequestToken()
                Log.d(TAG, "signIn: requestToken = $requestToken")
                requestToken?.let { token ->
                    val requestBody = ValidateTokenRequestBody(username, password, token)
                    if (validateToken(requestBody)) {
                        createSessionId(token)
                    }
                }

            } catch (e: Exception) {
                _signInNetworkState.value = NetworkState.Error(
                    mContext.getString(R.string.couldntSignIn)
                )
            }
        }
    }

    private suspend fun getRequestToken(): String? {
        return try {
            val response = repository.createRequestToken()
            Log.d(TAG, "getRequestToken: ${response.code()}")
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.success) {
                        return it.request_token
                    }
                }
            }
            _signInNetworkState.value = NetworkState.Error("Couldn't create request token")
            null
        } catch (e: Exception) {
            Log.d(TAG, "createRequestToken: ${e.message}")
            _signInNetworkState.value = NetworkState.Error("Couldn't create request token")
            null
        }
    }

    private suspend fun validateToken(requestBody: ValidateTokenRequestBody): Boolean {
        return try {
            val response = repository.validateToken(requestBody)
            Log.d(TAG, "validateToken: ${response.code()}")
            if (response.isSuccessful && response.body()?.success == true) {
                return true
            }
            _signInNetworkState.value = NetworkState.Error("Couldn't validate token")
            false
        } catch (e: Exception) {
            Log.d(TAG, "validateToken: ${e.message}")
            _signInNetworkState.value = NetworkState.Error("Couldn't validate token")
            false
        }
    }

    private suspend fun createSessionId(request_token: String) {
        try {
            val requestBody = SessionIdRequestBody(request_token)
            val response = repository.createSessionId(requestBody)
            Log.d(TAG, "createSessionId: ${response.code()}")
            if (response.isSuccessful) {
                _signInNetworkState.value = NetworkState.Success("Signed In")
                response.body()?.session_id?.let { sessionId ->
                    // save current sessionId to DataStore
                    dataStoreRepository.saveSessionId(sessionId)
                }
            }
        } catch (e: Exception) {
            _signInNetworkState.value = NetworkState.Error("Couldn't create session id")
            Log.d(TAG, "createSessionId: ${e.message}")
        }
    }


    //***************************************Profile Info*********************************
    fun getAccountDetails(sessionId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "getAccountDetails: sessionId = $sessionId")
                val response = repository.getAccountDetails(sessionId)
                Log.d(TAG, "getAccountDetails: ${response.code()} - ${response.message()}")
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _accountDetails.value = NetworkResult.Success(data)
                    } else {
                        _accountDetails.value = throwError(
                            mContext.getString(R.string.couldntGetAccountDetails)
                        )
                    }
                    val accountId = response.body()?.id ?: -1
                    dataStoreRepository.saveAccountId(accountId)
                } else {
                    _accountDetails.value = throwError(
                        mContext.getString(R.string.couldntGetAccountDetails)
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "getAccountDetails: ${e.message}")
                _accountDetails.value = throwError(
                    mContext.getString(R.string.couldntGetAccountDetails)
                )
            }
        }
    }

    fun deleteSession(sessionId: String) {
        try {
            _signOutNetworkState.value = NetworkState.Loading
            viewModelScope.launch {
                val response = repository.deleteSession(sessionId)
                val success = response.body()?.success
                Log.d(TAG, "deleteSession: response = ${response.code()}\nSuccess = $success")
                if (response.isSuccessful && success == true) {
                    dataStoreRepository.deleteSessionId()
                    _signOutNetworkState.value = NetworkState.Success("Signed Out")
                } else {
                    _signOutNetworkState.value = NetworkState.Error("Couldn't Sign Out")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "deleteSession exception: ${e.message}")
            _signOutNetworkState.value = NetworkState.Error("Couldn't Sign Out")
        }
    }

    private fun throwError(msg: String) = NetworkResult.Error(Exception(msg))

}