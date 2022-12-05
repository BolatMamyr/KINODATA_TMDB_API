package com.example.kinodata.model.tv

import com.example.kinodata.model.movie.RMovie

data class RTvSeries(
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
) {

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) return false
        other as RTvSeries
        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id
        result = 31 * result + overview.hashCode()
        return result
    }
}