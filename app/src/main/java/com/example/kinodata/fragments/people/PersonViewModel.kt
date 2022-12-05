package com.example.kinodata.fragments.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.credit.Person
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: Repository, private val personId: String) : ViewModel(){

    private val _person: MutableLiveData<Person> = MutableLiveData()
    val person: LiveData<Person> = _person

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
}