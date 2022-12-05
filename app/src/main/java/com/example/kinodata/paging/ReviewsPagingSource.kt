package com.example.kinodata.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.review.Review
import com.example.kinodata.repo.Repository

class ReviewsPagingSource(private val repository: Repository, private val movieId: String)
    : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val page = params.key ?: 1

        return try {
            val response = repository.getReviews(
                    movieId = movieId, language = MyConstants.LANGUAGE, page = page.toString()
                )
            val list = response.body()?.reviews
            return LoadResult.Page(
                data = list!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }


    }
}