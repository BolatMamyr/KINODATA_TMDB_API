package com.example.kinodata.fragments.profile.account

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.model.account.AccountDetails
import com.example.kinodata.model.auth.requestBodies.DeleteSessionRequestBody
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AccountViewModel"

@HiltViewModel
class AccountViewModel @Inject constructor(
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

    private val _accountDetails: MutableLiveData<AccountDetails> = MutableLiveData()
    val accountDetails: LiveData<AccountDetails> = _accountDetails

    // whenever it becomes true AccountFragment will be replaced with SignInFragment
    private val _signOutSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val signOutSuccess: LiveData<Boolean> = _signOutSuccess

    fun getAccountDetails() {
        try {
            viewModelScope.launch {
                val response = repository.getAccountDetails(sessionId)
                Log.d(TAG, "getAccountDetails: ${response.code()}")
                if (response.isSuccessful) {
                    _accountDetails.value = response.body()
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "getAccountDetails: ${e.message}")
        }
    }

    // Sign Out
    fun deleteSession() {

        try {
            viewModelScope.launch {
                val response = repository.deleteSession(sessionId)
                val success = response.body()?.success
                Log.d(
                    TAG,
                    "deleteSession: response = ${response.code()}\nSuccess = $success"
                )
                if (response.isSuccessful && success == true) {
                    dataStoreRepository.deleteSessionId()
                    _signOutSuccess.value = true
                }
                // Immediately becomes false after so that it will not always keep "true" val
                _signOutSuccess.value = false

            }
        } catch (e: Exception) {
            Log.d(TAG, "deleteSession exception: ${e.message}")
            _signOutSuccess.value = false
        }

    }
}