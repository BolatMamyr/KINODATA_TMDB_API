package com.example.kinodata.fragments.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    fun getMultiSearchResults(query: String?) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getMultiSearchResults(query = query, language = MyConstants.LANGUAGE, page = "1")
                Log.d("MyLog", "getMultiSearchResults: ${response.code()}")
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.results
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getMultiSearchResults: ${e.message}")
            }
        }
    }
}