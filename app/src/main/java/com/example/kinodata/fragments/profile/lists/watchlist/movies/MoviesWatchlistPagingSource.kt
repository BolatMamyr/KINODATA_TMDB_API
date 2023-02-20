package com.example.kinodata.fragments.profile.lists.watchlist.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.repo.Repository
import javax.inject.Inject

class MoviesWatchlistPagingSource(
    private val repository: Repository,
    private val accountId: Int,
    private val sessionId: String
) : PagingSource<Int, RMovie>() {
    override fun getRefreshKey(state: PagingState<Int, RMovie>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RMovie> {
        val page = params.key ?: 1

        return try {
            val response = repository.getMoviesWatchlist(accountId, sessionId, page)
            val data = response.body()?.results ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}