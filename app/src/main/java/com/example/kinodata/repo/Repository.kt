package com.example.kinodata.repo

import com.example.kinodata.api.MovieDataApi
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.account.accountDetails.AccountDetails
import com.example.kinodata.model.account.accountStates.AccountStates
import com.example.kinodata.model.auth.SuccessResponse
import com.example.kinodata.model.auth.RequestToken
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
import com.example.kinodata.model.auth.requestBodies.ValidateTokenRequestBody
import com.example.kinodata.model.account.favorite.AddToFavoriteRequestBody
import com.example.kinodata.model.account.rate.RateRequestBody
import com.example.kinodata.model.account.watchlist.AddToWatchlistRequestBody
import com.example.kinodata.model.collection.CollectionResult
import com.example.kinodata.model.images.ImageResult
import com.example.kinodata.model.images.PersonImageResult
import com.example.kinodata.model.persons.media_credits.Credits
import com.example.kinodata.model.persons.person.Person
import com.example.kinodata.model.persons.person.personMovies.PersonMovieCredits
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvSeriesCredits
import com.example.kinodata.model.movie.ResultForMovies
import com.example.kinodata.model.movie.movieDetails.MovieDetails
import com.example.kinodata.model.multiSearch.MultiSearch
import com.example.kinodata.model.persons.popular.ResultForPopularPersons
import com.example.kinodata.model.review.ReviewResult
import com.example.kinodata.model.tv.ResultForTv
import com.example.kinodata.model.tv.season.SeasonDetails
import com.example.kinodata.model.tv.tvDetails.TvDetails
import com.example.kinodata.model.videos.VideoResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject


class Repository @Inject constructor(private val api: MovieDataApi) {

    // *****************************MOVIES*******************************************

    suspend fun getPopularMovies(language: String, page: Int): Response<ResultForMovies> {
        return api.getPopularMovies(language = language, page = page)
    }

    suspend fun getTopRatedMovies(language: String, page: Int): Response<ResultForMovies> {
        return api.getTopRatedMovies(language = language, page = page)
    }

    suspend fun getNowPlayingMovies(language: String, page: Int): Response<ResultForMovies> {
        return api.getNowPlayingMovies(language = language, page = page)
    }

    suspend fun getUpcomingMovies(language: String, page: Int): Response<ResultForMovies> {
        return api.getUpcomingMovies(language = language, page = page)
    }

    suspend fun getMovieDetails(movieId: Int, language: String): Response<MovieDetails> {
        return api.getMovieDetails(movieId = movieId, language = language)
    }

    suspend fun getMovieCredits(movieId: Int, language: String): Response<Credits> {
        return api.getMovieCredits(movieId = movieId, language = language)
    }

    suspend fun getMovieReviews(
        id: String, language: String, page: String = "1"
    ): Response<ReviewResult> {
        return api.getMovieReviews(movieId = id, language = language)
    }

    suspend fun getMovieAccountStates(
        movieId: String,
        session_id: String
    ): Response<AccountStates> {
        return api.getMovieAccountStates(movieId = movieId, session_id = session_id)
    }

    suspend fun getMovieImages(
        movieId: Int
    ): Response<ImageResult> {
        return api.getMovieImages(movieId = movieId)
    }

    suspend fun getMovieRecommendations(movieId: Int): Response<ResultForMovies> {
        return api.getMovieRecommendations(movieId = movieId)
    }

    suspend fun getMovieVideos(movieId: Int): Response<VideoResult> {
        return api.getMovieVideos(movieId = movieId)
    }

    // **************************TV***************************************************
    suspend fun getPopularTv(language: String, page: Int): Response<ResultForTv> {
        return api.getPopularTv(language = language, page = page)
    }

    suspend fun getTopTv(language: String, page: Int): Response<ResultForTv> {
        return api.getTopTv(language = language, page = page)
    }

    suspend fun getAiringTv(language: String, page: Int): Response<ResultForTv> {
        return api.getAiringTv(language = language, page = page)
    }

    suspend fun getPersonInfo(personId: String, language: String): Response<Person> {
        return api.getPersonInfo(personId = personId, language = language)
    }

    suspend fun getPersonMovieCredits(
        personId: String, language: String
    ): Response<PersonMovieCredits> {
        return api.getPersonMovieCredits(personId = personId, language = language)
    }

    suspend fun getPersonTvSeriesCredits(
        personId: String, language: String
    ): Response<PersonTvSeriesCredits> {
        return api.getPersonTvSeriesCredits(
            personId = personId,
            language = language
        )
    }

    suspend fun getPersonImages(
        personId: Int
    ): Response<PersonImageResult> {
        return api.getPersonImages(personId = personId)
    }
    suspend fun getPopularPersons(
        language: String, page: String
    ): Response<ResultForPopularPersons> {
        return api.getPopularPersons(language = language, page = page)
    }

    suspend fun getTvDetails(
        tvId: String, language: String
    ): Response<TvDetails> {
        return api.getTvDetails(tvId = tvId, language = language)
    }

    suspend fun getTvCredits(
        tvId: String, language: String
    ): Response<Credits> {
        return api.getTvCredits(tvId = tvId, language = language)
    }

    suspend fun getTvReviews(
        tvId: String, language: String, page: String = "1"
    ): Response<ReviewResult> {
        return api.getTvReviews(tvId = tvId, language = language)
    }

    suspend fun getTvAccountStates(tvId: Int, session_id: String): Response<AccountStates> {
        return api.getTvAccountStates(tvId = tvId, session_id = session_id)
    }

    suspend fun getTvImages(
        tvId: Int
    ): Response<ImageResult> {
        return api.getTvImages(tvId = tvId)
    }

    suspend fun getTvRecommendations(tvId: Int): Response<ResultForTv> {
        return api.getTvRecommendations(tvId = tvId)
    }

    suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): Response<SeasonDetails> {
        return api.getSeasonDetails(tvId = tvId, seasonNumber = seasonNumber)
    }

    suspend fun getTvVideos(tvId: Int): Response<VideoResult> {
        return api.getTvVideos(tvId = tvId)
    }

    suspend fun getCollection(collectionId: Int): Response<CollectionResult> {
        return api.getCollection(collectionId = collectionId)
    }

    suspend fun getMultiSearchResults(
        query: String?, language: String, page: String = "1"
    ): Response<MultiSearch> {
        return api.getMultiSearchResults(query = query, language = language, page = page)
    }

    suspend fun createRequestToken(): Response<RequestToken> {
        return api.createRequestToken()
    }

    suspend fun validateToken(
        requestBody: ValidateTokenRequestBody
    ): Response<RequestToken> {
        return api.validateToken(
            requestBody = requestBody
        )
    }

    suspend fun createSessionId(requestBody: SessionIdRequestBody): Response<SessionIdResult> {
        return api.createSessionId(requestBody = requestBody)
    }

    //    suspend fun deleteSession(
//        requestBody: DeleteSessionRequestBody
//    ): Response<DeleteSessionResponse> {
//        return api.deleteSession(requestBody = requestBody)
//    }
    suspend fun deleteSession(
        session_id: String
    ): Response<SuccessResponse> {
        return api.deleteSession(session_id = session_id)
    }

    suspend fun getAccountDetails(
        session_id: String
    ): Response<AccountDetails> {
        return api.getAccountDetails(session_id = session_id)
    }

    suspend fun addToFavorite(
        accountId: Int,
        session_id: String,
        requestBody: AddToFavoriteRequestBody
    ): Response<SuccessResponse> {
        return api.addToFavorite(
            accountId = accountId,
            session_id = session_id,
            requestBody = requestBody
        )
    }

    suspend fun getFavoriteMovies(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForMovies> {
        return api.getFavoriteMovies(accountId = accountId, session_id = session_id, page = page)
    }

    suspend fun getFavoriteTv(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForTv> {
        return api.getFavoriteTv(accountId = accountId, session_id = session_id, page = page)
    }

    suspend fun addToWatchlist(
        accountId: Int,
        session_id: String,
        requestBody: AddToWatchlistRequestBody
    ): Response<SuccessResponse> {
        return api.addToWatchlist(
            accountId = accountId,
            session_id = session_id,
            requestBody = requestBody
        )
    }

    suspend fun rateMovie(
        movieId: String,
        session_id: String,
        requestBody: RateRequestBody
    ): Response<SuccessResponse> {
        return api.rateMovie(movieId = movieId, session_id = session_id, requestBody = requestBody)
    }
    suspend fun deleteMovieRating(
        movieId: String,
        session_id: String
    ): Response<SuccessResponse> {
        return api.deleteMovieRating(movieId = movieId, session_id = session_id)
    }
    suspend fun rateTv(
        tvId: String,
        session_id: String,
        requestBody: RateRequestBody
    ): Response<SuccessResponse> {
        return api.rateTv(tvId = tvId, session_id = session_id, requestBody = requestBody)
    }

    suspend fun deleteTvRating(
        tvId: String,
        session_id: String
    ): Response<SuccessResponse> {
        return api.deleteTvRating(tvId = tvId, session_id = session_id)
    }

    suspend fun getRatedMovies(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForMovies> {
        return api.getRatedMovies(accountId = accountId, session_id = session_id, page = page)
    }

    suspend fun getRatedTv(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForTv> {
        return api.getRatedTv(accountId = accountId, session_id = session_id, page = page)
    }
    suspend fun getMoviesWatchlist(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForMovies> {
        return api.getMoviesWatchlist(accountId = accountId, session_id = session_id, page = page)
    }

    suspend fun getTvWatchlist(
        accountId: Int,
        session_id: String,
        page: Int
    ): Response<ResultForTv> {
        return api.getTvWatchlist(accountId = accountId, session_id = session_id, page = page)
    }

//    suspend fun getPopularMovies(language: String, page: Int): Flow<Response<ResultForMovies>> {
//        return flow {
//            var retryCount = 0
//            while (true) {
//                try {
//                    emit(api.getPopularMovies(language = language, page = page))
//                    break
//                } catch (e: Exception) {
//                    retryCount++
//                    if (retryCount >= 3) {
//                        throw e
//                    } else {
//                        delay(1000L)
//                    }
//                }
//            }
//        }
//    }
}