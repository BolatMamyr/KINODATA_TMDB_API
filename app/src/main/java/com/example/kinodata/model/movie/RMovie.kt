package com.example.kinodata.model.movie

data class RMovie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) return false
        other as RMovie
        if (id != other.id) return false
        if (title != other.title) return false
        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + id
        result = 31 * result + overview.hashCode()
        return result
    }
}