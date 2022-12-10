package com.example.kinodata.api

import com.example.kinodata.constants.MyConstants
import com.example.kinodata.constants.MyConstants.Companion.API_KEY
import com.example.kinodata.model.credit.Credits
import com.example.kinodata.model.credit.person.Person
import com.example.kinodata.model.credit.person.personMovies.PersonMovieCredits
import com.example.kinodata.model.credit.person.personTvSeries.PersonTvSeriesCredits
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.review.ReviewResult
import com.example.kinodata.model.tv.ResultForTvSeries
import com.example.kinodata.model.tv.tvDetails.TvDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataApi {

    // *******************************Movies**************************************
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
    suspend fun getMovieCredits(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<Credits>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_REVIEWS)
    suspend fun getMovieReviews(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: String = "1"
    ): Response<ReviewResult>

    // *******************************TV Series**************************************
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

    @GET(MyConstants.URL_TV + "{tvId}")
    suspend fun getTvDetails(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<TvDetails>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_CREDITS)
    suspend fun getTvCredits(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<Credits>

    // *******************************Person**************************************

    @GET(MyConstants.URL_PERSON + "{personId}")
    suspend fun getPersonInfo(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<Person>

    @GET(MyConstants.URL_PERSON + "{personId}" + MyConstants.URL_PERSON_MOVIE_CREDITS)
    suspend fun getPersonMovieCredits(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<PersonMovieCredits>

    @GET(MyConstants.URL_PERSON + "{personId}" + MyConstants.URL_PERSON_TV_CREDITS)
    suspend fun getPersonTvSeriesCredits(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String
    ): Response<PersonTvSeriesCredits>

    // https://api.themoviedb.org/3//person/18918?api_key=f2d6d5225e43110cc809522d5ae31922
}