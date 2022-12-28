package com.example.kinodata.fragments.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.model.persons.popular.PopularPerson
import com.example.kinodata.paging.PopularPersonsPagingSource
import com.example.kinodata.repo.Repository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _popularPersons: MutableLiveData<List<PopularPerson>> = MutableLiveData()
    val popularPersons: LiveData<List<PopularPerson>> = _popularPersons

    fun getMultiSearchResults(query: String?) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getMultiSearchResults(query = query, language = MyConstants.LANGUAGE, page = "1")
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.results
                }
            } catch (e: Exception) {
                Log.d("MyLog", "Error: getMultiSearchResults: ${e.message}")
            }
        }
    }

    val popularPersonsPaging = Pager(PagingConfig(pageSize = 20)) {
        PopularPersonsPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getPopularPersons() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularPersons(MyConstants.LANGUAGE, "1")
                if (response.isSuccessful) {
                    _popularPersons.value = response.body()?.results
                    Log.d("MyLog", "getPopularPersons: ${response.code()}")
                } else {
                    Log.d("MyLog", "getPopularPersons: ${response.code()}")
                }
            } catch (_:Exception) {

            }
        }
    }
}