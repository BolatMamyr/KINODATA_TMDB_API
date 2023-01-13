package com.example.kinodata.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.kinodata.constants.MyConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
//    MyConstants.PREFERENCES_DATA_STORE_NAME
//)
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
    ) {

    private val sessionIdKey = stringPreferencesKey(MyConstants.SESSION_ID_PREFERENCE)
    private val isSignedInKey = booleanPreferencesKey(MyConstants.IS_SIGNED_IN_PREFERENCE)

    suspend fun saveSessionId(sessionId: String) {
        dataStore.edit { preference ->
            preference[sessionIdKey] = sessionId
            preference[isSignedInKey] = true
        }
    }

    val sessionId: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }
        }.map { preferences ->
            val sessionId = preferences[sessionIdKey] ?: "null"
            sessionId
        }

    val isSignedIn: Flow<Boolean> = dataStore.data
        .catch { exception ->
            Log.d("DataStore", exception.message.toString())
            emit(emptyPreferences())
        }.map {  preferences ->
            val isSignedIn = preferences[isSignedInKey] ?: false
            isSignedIn
        }

    suspend fun deleteSessionId() {
        dataStore.edit { preference ->
            preference[sessionIdKey] = "null"
            preference[isSignedInKey] = false
        }
    }

}