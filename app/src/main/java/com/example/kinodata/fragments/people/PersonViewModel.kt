package com.example.kinodata.fragments.people

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.person.Person
import com.example.kinodata.model.persons.person.personMovies.PersonActingMovies
import com.example.kinodata.model.persons.person.personMovies.PersonMovieCredits
import com.example.kinodata.model.persons.person.personMovies.PersonMoviesAsCrew
import com.example.kinodata.model.persons.person.personTvSeries.PersonActingTv
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvAsCrew
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvSeriesCredits
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PersonViewModel"
@HiltViewModel
class PersonViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository
    ) : ViewModel() {

    private val _personId = MutableStateFlow(0)
    val personId = _personId.asStateFlow()

    fun setPersonId(id: Int) {
        _personId.value = id
    }

    private val _person = MutableLiveData<NetworkResult<Person>>(NetworkResult.Loading)
    val person: LiveData<NetworkResult<Person>> = _person

    private val _movies = MutableLiveData<NetworkResult<PersonMovieCredits>>(NetworkResult.Loading)
    val movies: LiveData<NetworkResult<PersonMovieCredits>> = _movies

    private val _tv = MutableLiveData<NetworkResult<PersonTvSeriesCredits>>(NetworkResult.Loading)
    val tv: LiveData<NetworkResult<PersonTvSeriesCredits>> = _tv

    fun getPersonInfo(id: Int) {
        _person.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                        .getPersonInfo(personId = id.toString(), language = MyConstants.LANGUAGE)

                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _person.value = NetworkResult.Success(data)
                } else {
                    _person.value = throwError(mContext.getString(R.string.couldntGetPersonDetails))
                }
            } catch (e: Exception) {
                _person.value = NetworkResult.Error(e)
            }
        }
    }

    // get persons' movies both as cast and crew member
    fun getPersonMovieCredits(id: Int) {
        _movies.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                        .getPersonMovieCredits(id.toString(), MyConstants.LANGUAGE)

                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _movies.value = NetworkResult.Success(data)
                } else {
                    _movies.value = throwError(mContext.getString(R.string.couldntGetPersonsMovies))
                }
            } catch (e: Exception) {
                _movies.value = NetworkResult.Error(e)
            }
        }
    }

    // get persons' tv both as cast and crew member
    fun getPersonTvSeriesCredits(id: Int) {
        _tv.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                        .getPersonTvSeriesCredits(id.toString(), MyConstants.LANGUAGE)

                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _tv.value = NetworkResult.Success(data)
                } else {
                    _tv.value = throwError(mContext.getString(R.string.couldntGetPersonsTv))
                }
            } catch (e: Exception) {
                _tv.value = NetworkResult.Error(e)
            }
        }
    }

    private fun throwError(msg: String) = NetworkResult.Error(Exception(msg))

}