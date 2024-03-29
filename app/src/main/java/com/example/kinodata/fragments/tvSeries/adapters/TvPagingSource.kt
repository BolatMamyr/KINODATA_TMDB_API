package com.example.kinodata.fragments.tvSeries.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.model.tv.ResultForTv
import com.example.kinodata.repo.Repository
import retrofit2.Response
import javax.inject.Inject

class TvPagingSource @Inject constructor(
    private val category: String,
    private val repository: Repository
    )
    : PagingSource<Int, RTvSeries>() {

    override fun getRefreshKey(state: PagingState<Int, RTvSeries>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RTvSeries> {
        val page = params.key ?: 1

        return try {
            var data: Response<ResultForTv>? = null
            if (category == MyConstants.POPULAR) {
                data = repository.getPopularTv(language = MyConstants.LANGUAGE, page = page)
            }
            if (category == MyConstants.TOP_RATED) {
                data = repository.getTopTv(language = MyConstants.LANGUAGE, page = page)
            }
            if (category == MyConstants.NOW_PLAYING) {
                data = repository.getAiringTv(language = MyConstants.LANGUAGE, page = page)
            }
            val list = data?.body()?.results ?: emptyList()
            LoadResult.Page(
                data = list,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}