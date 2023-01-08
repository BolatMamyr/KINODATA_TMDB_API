package com.example.kinodata.fragments.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
    )
    : AndroidViewModel(application) {

    // TODO: some LiveData with Boolean value to store any failure to report user with Toast message

    private val TAG = "ProfileViewModel"

    private val dataStoreRepository = DataStoreRepository(application)

    private val _sessionIdResult: MutableLiveData<SessionIdResult> = MutableLiveData()
    val sessionIdResult: LiveData<SessionIdResult> = _sessionIdResult

    val sessionId: LiveData<String> = dataStoreRepository.readFromDataStore.asLiveData()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            try {
                val requestToken = getRequestToken()
                Log.d(TAG, "signIn: requestToken = $requestToken")
                requestToken?.let {
                    // TODO: create data class not hashmap
                    val requestBody = HashMap<String, String>()
                    requestBody["username"] = username
                    requestBody["password"] = password
                    requestBody["request_token"] = it

                    if (validateToken(requestBody)) {
                        createSessionId(it)
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, "signIn: ${e.message}")
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
            null
        } catch (e: Exception) {
            Log.d(TAG, "createRequestToken: ${e.message}")
            null
        }
    }

    // returns true if validated
    private suspend fun validateToken(userInfo: HashMap<String, String>): Boolean {
        return try {
            val response = repository.validateToken(userInfo)
            Log.d(TAG, "validateToken: ${response.code()}")
            if (response.isSuccessful) {
                return response.body()?.success == true
            }
            false
        } catch (e: Exception) {
            Log.d(TAG, "validateToken: ${e.message}")
            false
        }
    }

    private suspend fun createSessionId(request_token: String) {
        try {
            val requestBody = SessionIdRequestBody(request_token)
            val response = repository.createSessionId(requestBody)
            if (response.isSuccessful) {
                _sessionIdResult.value = response.body()
                response.body()?.session_id?.let { sessionId ->
                    dataStoreRepository.saveSessionId(sessionId)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "createSessionId: ${e.message}")
        }
    }

    data class SessionIdRequestBody(val request_token: String)

}