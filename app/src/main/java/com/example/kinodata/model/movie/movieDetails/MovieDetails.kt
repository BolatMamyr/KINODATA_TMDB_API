package com.example.kinodata.model.movie.movieDetails

data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    fun getGenres() : String {
        val list = genres.map { it.name }
        var result = ""
        for ((index, s) in list.withIndex()) {
            if (index == 0) {
                result += s
            } else {
                result += ", $s"
            }
        }
        return result
    }

    fun getCountries(): String {
        val list = production_countries.map { it.name }
        var result = ""
        for ((index, s) in list.withIndex()) {
            if (index == 0) {
                result += s
            } else {
                result += ", $s"
            }
        }
        return result
    }
}