package com.example.kinodata.repo

import com.example.kinodata.api.MovieDataApi
import com.example.kinodata.model.auth.DeleteSessionResponse
import com.example.kinodata.model.auth.RequestToken
import com.example.kinodata.model.auth.SessionIdResult
import com.example.kinodata.model.auth.requestBodies.DeleteSessionRequestBody
import com.example.kinodata.model.auth.requestBodies.SessionIdRequestBody
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
import javax.inject.Inject


class Repository @Inject constructor(private val api: MovieDataApi) {

    init {
        println("Repository initialized")
    }
//    suspend fun getPopularMovies(language: String, page: String): Flow<Response<ResultForMovies>> {
//        return flow {
//            var retryCount = 0
//            while (true) {
//                try {
//                    emit(RetrofitInstance.api.getPopularMovies(language = language, page = page))
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

    suspend fun getPopularMovies(language: String, page: String): Response<ResultForMovies> {
        return api.getPopularMovies(language = language, page = page)
    }

    suspend fun getTopRatedMovies(language: String, page: String): Response<ResultForMovies> {
        return api.getTopRatedMovies(language = language, page = page)
    }

    suspend fun getNowPlayingMovies(language: String, page: String): Response<ResultForMovies> {
        return api.getNowPlayingMovies(language = language, page = page)
    }

    suspend fun getUpcomingMovies(language: String, page: String): Response<ResultForMovies> {
        return api.getUpcomingMovies(language = language, page = page)
    }

    suspend fun getMovieDetails(movieId: String, language: String): Response<MovieDetails> {
        return api.getMovieDetails(movieId = movieId, language = language)
    }

    suspend fun getMovieCredits(movieId: String, language: String): Response<Credits> {
        return api.getMovieCredits(movieId = movieId, language = language)
    }

    suspend fun getMovieReviews(
        id: String, language: String, page: String = "1"
    ): Response<ReviewResult> {
        return api.getMovieReviews(movieId = id, language = language)
    }

    suspend fun getPopularTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return api.getPopularTvSeries(language = language, page = page)
    }

    suspend fun getTopRatedTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return api.getTopRatedTvSeries(language = language, page = page)
    }

    suspend fun getAiringTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return api.getAiringTvSeries(language = language, page = page)
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

    suspend fun getMultiSearchResults(
        query: String?, language: String, page: String = "1"
    ): Response<MultiSearch> {
        return api.getMultiSearchResults(query = query, language = language, page = page)
    }

    suspend fun createRequestToken(): Response<RequestToken> {
        return api.createRequestToken()
    }

    suspend fun validateToken(
        requestBody: HashMap<String, String>
    ): Response<RequestToken> {
        return api.validateToken(
            requestBody = requestBody
        )
    }

    suspend fun createSessionId(requestBody: SessionIdRequestBody): Response<SessionIdResult> {
        return api.createSessionId(requestBody = requestBody)
    }

    suspend fun deleteSession(
        requestBody: DeleteSessionRequestBody
    ): Response<DeleteSessionResponse> {
        return api.deleteSession(requestBody = requestBody)
    }

}