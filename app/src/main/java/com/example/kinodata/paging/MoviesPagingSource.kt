package com.example.kinodata.paging

import androidx.paging.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.constants.MyConstants.Companion.LANGUAGE
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.repo.Repository
import retrofit2.Response

class MoviesPagingSource(private val category: String, private val repository: Repository)
    : PagingSource<Int, RMovie> () {

    override fun getRefreshKey(state: PagingState<Int, RMovie>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RMovie> {
        val page = params.key ?: 1

        return try {
            var data: Response<ResultForMovies>? = null
            if (category == MyConstants.POPULAR) {
                data = repository.getPopularMovies(LANGUAGE, page.toString())
            }
            if (category == MyConstants.TOP_RATED) {
                data = repository.getTopRatedMovies(LANGUAGE, page.toString())
            }
            if (category == MyConstants.NOW_PLAYING) {
                data = repository.getNowPlayingMovies(LANGUAGE, page.toString())
            }
            if (category == MyConstants.UPCOMING) {
                data = repository.getUpcomingMovies(LANGUAGE, page.toString())
            }

            LoadResult.Page(
                data = data?.body()?.results!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.results?.isEmpty()!!) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}