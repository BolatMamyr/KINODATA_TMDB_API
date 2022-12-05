package com.example.kinodata.api

import com.example.kinodata.constants.MyConstants
import com.example.kinodata.constants.MyConstants.Companion.API_KEY
import com.example.kinodata.model.credit.Credit
import com.example.kinodata.model.credit.Person
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.model.movieDetails.MovieDetails
import com.example.kinodata.model.review.ReviewResult
import com.example.kinodata.model.tv.ResultForTvSeries
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataApi {
    @GET(MyConstants.URL_POPULAR)
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_TOP_RATED)
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_UPCOMING)
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_MOVIE + "{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<MovieDetails>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_CREDITS)
    suspend fun getCredits(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<Credit>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_REVIEWS)
    suspend fun getReviews(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String = "1"
    ): Response<ReviewResult>


    @GET(MyConstants.URL_TV_POPULAR)
    suspend fun getPopularTvSeries(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_TV_TOP_RATED)
    suspend fun getTopRatedTvSeries(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_TV_AIRING)
    suspend fun getAiringTvSeries(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_PERSON + "{personId}")
    suspend fun getPersonInfo(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<Person>

    // https://api.themoviedb.org/3//person/18918?api_key=f2d6d5225e43110cc809522d5ae31922
}