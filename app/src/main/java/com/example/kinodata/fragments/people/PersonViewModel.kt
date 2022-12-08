package com.example.kinodata.fragments.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.credit.person.Person
import com.example.kinodata.model.credit.person.personMovies.PersonMovies
import com.example.kinodata.model.credit.person.personTvSeries.PersonTvSeries
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: Repository, private val personId: String) : ViewModel(){

    private val _person: MutableLiveData<Person> = MutableLiveData()
    val person: LiveData<Person> = _person

    private val _movies: MutableLiveData<List<PersonMovies>> = MutableLiveData()
    val movies: LiveData<List<PersonMovies>> = _movies

    private val _tvSeries: MutableLiveData<List<PersonTvSeries>> = MutableLiveData()
    val tvSeries: LiveData<List<PersonTvSeries>> = _tvSeries

    fun getPersonInfo() {
        viewModelScope.launch {
            try {
                val response = repository
                    .getPersonInfo(personId = personId, language = MyConstants.LANGUAGE)
                Log.d("MyLog", "getPersonInfo: ${response.message()}  ${response.code()}")
                if (response.isSuccessful) {
                    _person.value = response.body()
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getPersonMovieCredits() {
        viewModelScope.launch {
            try {
                val response = repository
                    .getPersonMovieCredits(personId, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    _movies.value = response.body()?.cast
                }
            } catch (_: Exception) {

            }
        }
    }

    fun getPersonTvSeriesCredits() {
        viewModelScope.launch {
            try {
                val response = repository
                    .getPersonTvSeriesCredits(personId, MyConstants.LANGUAGE)
                if (response.isSuccessful) {
                    _tvSeries.value = response.body()?.cast
                }
            } catch (_: Exception) {

            }
        }
    }
}