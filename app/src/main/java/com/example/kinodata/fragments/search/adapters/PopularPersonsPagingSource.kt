package com.example.kinodata.fragments.search.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.popular.PopularPerson
import com.example.kinodata.repo.Repository
import javax.inject.Inject

class PopularPersonsPagingSource @Inject constructor(
    private val repository: Repository
): PagingSource<Int, PopularPerson>() {


    override fun getRefreshKey(state: PagingState<Int, PopularPerson>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularPerson> {
        val page = params.key ?: 1

        return try {
            val response = repository.getPopularPersons(MyConstants.LANGUAGE, page.toString())
            val list = response.body()?.results
            LoadResult.Page(
                data = list!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}