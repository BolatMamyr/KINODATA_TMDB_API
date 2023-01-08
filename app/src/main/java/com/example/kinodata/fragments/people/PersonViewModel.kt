package com.example.kinodata.fragments.people

import androidx.lifecycle.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.person.Person
import com.example.kinodata.model.persons.person.personMovies.PersonActingMovies
import com.example.kinodata.model.persons.person.personMovies.PersonMoviesAsCrew
import com.example.kinodata.model.persons.person.personTvSeries.PersonActingTv
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvAsCrew
import com.example.kinodata.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: Repository
    ) : ViewModel() {

    private val personId = state.get<Int>("personId")?.toString() ?: "null"


    private val _person: MutableLiveData<Person> = MutableLiveData()
    val person: LiveData<Person> = _person

    private val _actingMovies: MutableLiveData<List<PersonActingMovies>> = MutableLiveData()
    val actingMovies: LiveData<List<PersonActingMovies>> = _actingMovies

    private val _moviesAsCrew: MutableLiveData<List<PersonMoviesAsCrew>> = MutableLiveData()
    val moviesAsCrew: LiveData<List<PersonMoviesAsCrew>> = _moviesAsCrew

    private val _actingTv: MutableLiveData<List<PersonActingTv>> = MutableLiveData()
    val actingTv: LiveData<List<PersonActingTv>> = _actingTv

    private val _tvAsCrew: MutableLiveData<List<PersonTvAsCrew>> = MutableLiveData()
    val tvAsCrew: LiveData<List<PersonTvAsCrew>> = _tvAsCrew

    fun getPersonInfo() {
        viewModelScope.launch {
            try {
                val response = repository
                        .getPersonInfo(personId = personId, language = MyConstants.LANGUAGE)

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
                    _actingMovies.value = response.body()?.cast
                    _moviesAsCrew.value = response.body()?.crew
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
                    _actingTv.value = response.body()?.cast
                    _tvAsCrew.value = response.body()?.crew
                }
            } catch (_: Exception) {

            }
        }
    }

}