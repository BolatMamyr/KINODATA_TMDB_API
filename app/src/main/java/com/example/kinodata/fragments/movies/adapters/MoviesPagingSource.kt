package com.example.kinodata.fragments.movies.adapters

import android.util.Log
import androidx.paging.*
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.repo.Repository
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import javax.inject.Inject

class MoviesPagingSource(
    private val category: String,
    private val repository: Repository
    ) : PagingSource<Int, RMovie> () {



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
                data = repository.getPopularMovies(MyConstants.LANGUAGE, page)
            }
            if (category == MyConstants.TOP_RATED) {
                data = repository.getTopRatedMovies(MyConstants.LANGUAGE, page)
            }
            if (category == MyConstants.NOW_PLAYING) {
                data = repository.getNowPlayingMovies(MyConstants.LANGUAGE, page)
            }
            if (category == MyConstants.UPCOMING) {
                data = repository.getUpcomingMovies(MyConstants.LANGUAGE, page)
            }
            Log.d("VerticalList", "response code = ${data?.code()}")
            if (data == null) {
                Log.d("VerticalList", "load = null")
            } else {
                Log.d("VerticalList", "load = not null - ${data.body()?.results?.size}")
            }
            Log.d("VerticalList", "load: ${data?.body()?.results}")

            LoadResult.Page(
                data = data?.body()?.results!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.body()?.results?.isEmpty()!!) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("VerticalList", "load: ${e.message}")
            LoadResult.Error(e)
        }
    }


}