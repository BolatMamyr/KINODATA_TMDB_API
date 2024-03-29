package com.example.kinodata.api

import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.account.accountDetails.AccountDetails
import com.example.kinodata.model.account.accountStates.AccountStates
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.auth.RequestToken
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.model.auth.requestBodies.ValidateTokenRequestBody
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.rate.RateRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
import com.example.kinodata.model.collection.CollectionResult
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.persons.person.Person
import com.example.kinodata.model.persons.person.personMovies.PersonMovieCredits
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvSeriesCredits
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.model.images.ImageResult
import com.example.kinodata.model.images.PersonImageResult
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.multiSearch.MultiSearch
import com.example.kinodata.model.persons.popular.ResultForPopularPersons
import com.example.kinodata.model.review.ReviewResult
import com.example.kinodata.model.tv.ResultForTv
import com.example.kinodata.model.tv.season.SeasonDetails
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.model.videos.VideoResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// TODO: Migrate to v4 Auth to get lists or delete lists and go further adding videos, images etc.
// TODO: Change search
interface MovieDataApi {

    // *******************************Movies**************************************
    @GET(MyConstants.URL_POPULAR)
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_TOP_RATED)
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_UPCOMING)
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_MOVIE + "{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String
    ): Response<MovieDetails>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_CREDITS)
    suspend fun getMovieCredits(
        @Path("movieId") movieId: Int,
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

    @POST(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_RATING)
    suspend fun rateMovie(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Body requestBody: RateRequestBody
    ): Response<SuccessResponse>

    @DELETE(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_RATING)
    suspend fun deleteMovieRating(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String
    ): Response<SuccessResponse>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_IMAGES)
    suspend fun getMovieImages(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<ImageResult>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_RECOMMENDATIONS)
    suspend fun getMovieRecommendations(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_MOVIE + "{movieId}" + MyConstants.URL_VIDEOS)
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<VideoResult>

    // *******************************TV Series**************************************
    @GET(MyConstants.URL_TV_POPULAR)
    suspend fun getPopularTv(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForTv>

    @GET(MyConstants.URL_TV_TOP_RATED)
    suspend fun getTopTv(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForTv>

    @GET(MyConstants.URL_TV_AIRING)
    suspend fun getAiringTv(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResultForTv>

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

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_ACCOUNT_STATES)
    suspend fun getTvAccountStates(
        @Path("tvId") tvId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String
    ): Response<AccountStates>

    @POST(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_RATING)
    suspend fun rateTv(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Body requestBody: RateRequestBody
    ): Response<SuccessResponse>

    @DELETE(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_RATING)
    suspend fun deleteTvRating(
        @Path("tvId") tvId: String,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String
    ): Response<SuccessResponse>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_IMAGES)
    suspend fun getTvImages(
        @Path("tvId") tvId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<ImageResult>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_RECOMMENDATIONS)
    suspend fun getTvRecommendations(
        @Path("tvId") tvId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<ResultForTv>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_SEASON + "{seasonNumber}")
    suspend fun getSeasonDetails(
        @Path("tvId") tvId: Int,
        @Path("seasonNumber") seasonNumber: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<SeasonDetails>

    @GET(MyConstants.URL_TV + "{tvId}" + MyConstants.URL_VIDEOS)
    suspend fun getTvVideos(
        @Path("tvId") tvId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<VideoResult>

    // ******************************Other****************************************

    @GET(MyConstants.URL_COLLECTION + "{collectionId}")
    suspend fun getCollection(
        @Path("collectionId") collectionId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<CollectionResult>

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
    suspend fun getPopularPersons(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<ResultForPopularPersons>

    @GET(MyConstants.URL_PERSON + "{personId}" + MyConstants.URL_IMAGES)
    suspend fun getPersonImages(
        @Path("personId") personId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<PersonImageResult>
    // ***********************Search********************************************
    @GET(MyConstants.URL_SEARCH_MULTI)
    suspend fun getMultiSearchResults(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("language") language: String,
        @Query("query") query: String?,
        @Query("page") page: String = "1"
    ): Response<MultiSearch>

    // *************************Authentication**********************************
    @GET(MyConstants.URL_REQUEST_TOKEN)
    suspend fun createRequestToken(
        @Query("api_key") api_key: String = MyConstants.API_KEY
    ): Response<RequestToken>

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
        @Query("session_id") session_id: String
    ): Response<SuccessResponse>

    // *************************Account**********************************
    @GET(MyConstants.URL_ACCOUNT)
    suspend fun getAccountDetails(
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String
    ): Response<AccountDetails>

    @POST(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_FAVORITE)
    suspend fun addToFavorite(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Body requestBody: AddToFavoriteRequestBody
    ): Response<SuccessResponse>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_FAVORITE_MOVIES)
    suspend fun getFavoriteMovies(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_FAVORITE_TV)
    suspend fun getFavoriteTv(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForTv>

    @POST(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_WATCHLIST)
    suspend fun addToWatchlist(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Body requestBody: AddToWatchlistRequestBody
    ): Response<SuccessResponse>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_WATCHLIST_MOVIES)
    suspend fun getMoviesWatchlist(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_WATCHLIST_TV)
    suspend fun getTvWatchlist(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForTv>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_RATED_MOVIES)
    suspend fun getRatedMovies(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForMovies>

    @GET(MyConstants.URL_ACCOUNT + "/{accountId}" + MyConstants.URL_RATED_TV)
    suspend fun getRatedTv(
        @Path("accountId") accountId: Int,
        @Query("api_key") api_key: String = MyConstants.API_KEY,
        @Query("session_id") session_id: String,
        @Query("page") page: Int
    ): Response<ResultForTv>


}