package com.example.kinodata.fragments.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.model.persons.popular.PopularPerson
import com.example.kinodata.fragments.search.adapters.PopularPersonsPagingSource
import com.example.kinodata.model.multiSearch.MultiSearch
import com.example.kinodata.repo.Repository
import com.example.kinodata.utils.MyUtils
import com.example.kinodata.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val mContext: Context,
    private val repository: Repository
    ): ViewModel() {

    private val _searchResults = MutableLiveData<NetworkResult<MultiSearch>>(NetworkResult.Loading)
    val searchResults: LiveData<NetworkResult<MultiSearch>> = _searchResults

    private val _popularPersons: MutableLiveData<List<PopularPerson>> = MutableLiveData()
    val popularPersons: LiveData<List<PopularPerson>> = _popularPersons

    fun getMultiSearchResults(query: String) {
        _searchResults.value = NetworkResult.Loading
        viewModelScope.launch {
            try {
                val response = repository
                    .getMultiSearchResults(query = query, language = MyConstants.LANGUAGE, page = "1")
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    _searchResults.value = NetworkResult.Success(data)
                } else {
                    _searchResults.value = MyUtils.throwError(
                        mContext.getString(R.string.errorGettingSearchResults)
                    )
                }
            } catch (e: Exception) {
                _searchResults.value = NetworkResult.Error(e)
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