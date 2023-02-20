package com.example.kinodata.fragments.profile.lists.favorite.tv

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.repo.Repository

class FavoriteTvPagingSource(
    private val repository: Repository,
    private val accountId: Int,
    private val sessionId: String
) : PagingSource<Int, RTvSeries>() {

    override fun getRefreshKey(state: PagingState<Int, RTvSeries>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RTvSeries> {
        val page = params.key ?: 1

        return try {
            val response = repository.getFavoriteTv(accountId, sessionId, page)
            val data = response.body()?.results
            LoadResult.Page(
                data = data ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data?.isEmpty()!!) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}