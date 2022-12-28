package com.example.kinodata.repo

import com.example.kinodata.api.RetrofitInstance
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


class Repository {

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
        return RetrofitInstance.api.getPopularMovies(language = language, page = page)
    }

    suspend fun getTopRatedMovies(language: String, page: String): Response<ResultForMovies> {
        return RetrofitInstance.api.getTopRatedMovies(language = language, page = page)
    }

    suspend fun getNowPlayingMovies(language: String, page: String): Response<ResultForMovies> {
        return RetrofitInstance.api.getNowPlayingMovies(language = language, page = page)
    }

    suspend fun getUpcomingMovies(language: String, page: String): Response<ResultForMovies> {
        return RetrofitInstance.api.getUpcomingMovies(language = language, page = page)
    }

    suspend fun getMovieDetails(movieId: String, language: String): Response<MovieDetails> {
        return RetrofitInstance.api.getMovieDetails(movieId = movieId, language = language)
    }

    suspend fun getMovieCredits(movieId: String, language: String): Response<Credits> {
        return RetrofitInstance.api.getMovieCredits(movieId = movieId, language = language)
    }

    suspend fun getMovieReviews(
        id: String, language: String, page: String = "1"
    ): Response<ReviewResult> {
        return RetrofitInstance.api.getMovieReviews(movieId = id, language = language)
    }

    suspend fun getPopularTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return RetrofitInstance.api.getPopularTvSeries(language = language, page = page)
    }

    suspend fun getTopRatedTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return RetrofitInstance.api.getTopRatedTvSeries(language = language, page = page)
    }

    suspend fun getAiringTvSeries(language: String, page: String): Response<ResultForTvSeries> {
        return RetrofitInstance.api.getAiringTvSeries(language = language, page = page)
    }

    suspend fun getPersonInfo(personId: String, language: String): Response<Person> {
        return RetrofitInstance.api.getPersonInfo(personId = personId, language = language)
    }

    suspend fun getPersonMovieCredits(
        personId: String, language: String
    ): Response<PersonMovieCredits> {
        return RetrofitInstance.api.getPersonMovieCredits(personId = personId, language = language)
    }

    suspend fun getPersonTvSeriesCredits(
        personId: String, language: String
    ): Response<PersonTvSeriesCredits> {
        return RetrofitInstance.api.getPersonTvSeriesCredits(
            personId = personId,
            language = language
        )
    }

    suspend fun getPopularPersons(
        language: String, page: String
    ): Response<ResultForPopularPersons> {
        return RetrofitInstance.api.getPopularPersons(language = language, page = page)
    }

    suspend fun getTvDetails(
        tvId: String, language: String
    ): Response<TvDetails> {
        return RetrofitInstance.api.getTvDetails(tvId = tvId, language = language)
    }

    suspend fun getTvCredits(
        tvId: String, language: String
    ): Response<Credits> {
        return RetrofitInstance.api.getTvCredits(tvId = tvId, language = language)
    }

    suspend fun getTvReviews(
        tvId: String, language: String, page: String = "1"
    ): Response<ReviewResult> {
        return RetrofitInstance.api.getTvReviews(tvId = tvId, language = language)
    }

    suspend fun getMultiSearchResults(
        query: String?, language: String, page: String = "1"
    ): Response<MultiSearch> {
        return RetrofitInstance.api.getMultiSearchResults(query = query, language = language, page = page)
    }

}