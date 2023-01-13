package com.example.kinodata.fragments.profile.signIn

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.repo.DataStoreRepository
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val app: Application,
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
    )
    : AndroidViewModel(app) {

    // TODO: some LiveData with Boolean value to store any failure to report user with Toast message

    private val _sessionIdResult: MutableLiveData<SessionIdResult> = MutableLiveData()
    val sessionIdResult: LiveData<SessionIdResult> = _sessionIdResult

    val sessionId: LiveData<String> = dataStoreRepository.sessionId.asLiveData()

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
                    } else {
                        makeToast()
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, "signIn: ${e.message}")
                makeToast()
            }
        }
    }

    private fun makeToast() {
        app.apply {
            Toast.makeText(
                this, "Couldn't sign in", Toast.LENGTH_SHORT
            ).show()
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
            makeToast()
            null
        } catch (e: Exception) {
            Log.d(TAG, "createRequestToken: ${e.message}")
            makeToast()
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
            makeToast()
            false
        } catch (e: Exception) {
            Log.d(TAG, "validateToken: ${e.message}")
            makeToast()
            false
        }
    }

    private suspend fun createSessionId(request_token: String) {
        try {
            val requestBody = SessionIdRequestBody(request_token)
            val response = repository.createSessionId(requestBody)
            Log.d(TAG, "createSessionId: ${response.code()}")
            if (response.isSuccessful) {
                _sessionIdResult.value = response.body()
                response.body()?.session_id?.let { sessionId ->
                    // save current sessionId to DataStore
                    dataStoreRepository.saveSessionId(sessionId)
                }
            } else makeToast()
        } catch (e: Exception) {
            Log.d(TAG, "createSessionId: ${e.message}")
            makeToast()
        }
    }


}