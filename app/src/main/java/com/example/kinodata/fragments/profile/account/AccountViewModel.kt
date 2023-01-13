package com.example.kinodata.fragments.profile.account

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.model.auth.requestBodies.DeleteSessionRequestBody
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AccountViewModel"

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _signOutSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val signOutSuccess: LiveData<Boolean> = _signOutSuccess

    fun deleteSession() {
        viewModelScope.launch {
            try {
                dataStoreRepository.sessionId.collect { sessionId ->
                    val response = repository.deleteSession(DeleteSessionRequestBody(sessionId))
                    val success = response.body()?.success
                    if (response.isSuccessful && success == true) {
                        dataStoreRepository.deleteSessionId()
                        _signOutSuccess.value = true
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, "deleteSession: ${e.message}")
                _signOutSuccess.value = false
            }
        }
    }
}