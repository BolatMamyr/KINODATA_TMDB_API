package com.example.kinodata.api

import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.account.AccountDetails
import com.example.kinodata.model.account.accountStates.AccountStates
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.auth.RequestToken
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.model.auth.requestBodies.ValidateTokenRequestBody
import com.example.kinodata.model.favorite.AddOrRemoveFromFavoriteRequestBody
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.persons.person.Person
import com.example.kinodata.model.persons.person.personMovies.PersonMovieCredits
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvSeriesCredits
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.multiSearch.MultiSearch
import com.example.kinodata.model.persons.popular.ResultForPopularPersons
import com.example.kinodata.model.review.ReviewResult
import com.example.kinodata.model.tv.ResultForTvSeries
import com.example.kinodata.model.tv.tvDetails.TvDetails
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataApi {

    // *******************************Movies**************************************
    @GET(MyConstants.URL_POPULAR)
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_TOP_RATED)
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_UPCOMING)
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_MOVIE + "{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<MovieDetails>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_CREDITS)
    suspend fun getMovieCredits(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<Credits>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_REVIEWS)
    suspend fun getMovieReviews(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String = "1"
    ): Response<ReviewResult>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_ACCOUNT_STATES)
    suspend fun getMovieAccountStates(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String
    ): Response<AccountStates>

    // *******************************TV Series**************************************
    @GET(MyConstants.URL_TV_POPULAR)
    suspend fun getPopularTvSeries(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_TV_TOP_RATED)
    suspend fun getTopRatedTvSeries(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_TV_AIRING)
    suspend fun getAiringTvSeries(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForTvSeries>

    @GET(MyConstants.URL_TV + "{tvId}")
    suspend fun getTvDetails(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<TvDetails>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_CREDITS)
    suspend fun getTvCredits(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<Credits>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_REVIEWS)
    suspend fun getTvReviews(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String = "1"
    ): Response<ReviewResult>
    // *******************************Person**************************************

    @GET(MyConstants.URL_PERSON + "{personId}")
    suspend fun getPersonInfo(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<Person>

    @GET(MyConstants.URL_PERSON + "{personId}" + MyConstants.URL_PERSON_MOVIE_CREDITS)
    suspend fun getPersonMovieCredits(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<PersonMovieCredits>

    @GET(MyConstants.URL_PERSON + "{personId}" + MyConstants.URL_PERSON_TV_CREDITS)
    suspend fun getPersonTvSeriesCredits(
        @Path("personId") personId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<PersonTvSeriesCredits>

    @GET(MyConstants.URL_PERSON_POPULAR)
    suspend fun  getPopularPersons(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForPopularPersons>

    // ***********************Search********************************************
    @GET(MyConstants.URL_SEARCH_MULTI)
    suspend fun getMultiSearchResults(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("query") query: String?,
        @Query("page") page: String = "1"
    ): Response<MultiSearch>

    @GET(MyConstants.URL_REQUEST_TOKEN)
    suspend fun createRequestToken(
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ):Response<RequestToken>

    @POST(MyConstants.URL_VALIDATE_TOKEN)
    suspend fun validateToken(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Body requestBody: ValidateTokenRequestBody
        ): Response<RequestToken>

    @POST(MyConstants.URL_CREATE_SESSION_ID)
    suspend fun createSessionId(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Body requestBody: SessionIdRequestBody
    ): Response<SessionIdResult>

    @DELETE(MyConstants.URL_DELETE_SESSION)
    suspend fun deleteSession(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query ("session_id") session_id: String
    ): Response<SuccessResponse>

    @GET(MyConstants.URL_ACCOUNT)
    suspend fun getAccountDetails(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query ("session_id") session_id: String
    ): Response<AccountDetails>

    @GET(MyConstants.URL_ACCOUNT + "{accountId}" + MyConstants.URL_FAVORITE_MOVIES)
    suspend fun getFavoriteMovies(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_ACCOUNT + "{accountId}" + MyConstants.URL_FAVORITE_TV)
    suspend fun getFavoriteTv(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForTvSeries>

    @POST(MyConstants.URL_ACCOUNT + "{accountId}" + MyConstants.URL_FAVORITE)
    suspend fun addOrRemoveFromFavorite(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Body requestBody: AddOrRemoveFromFavoriteRequestBody
    ):Response<SuccessResponse>
}