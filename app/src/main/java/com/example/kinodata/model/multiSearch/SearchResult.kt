package com.example.kinodata.model.multiSearch

import com.example.kinodata.constants.MyConstants

data class SearchResult(
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val gender: Int,
    val genre_ids: List<Int>,
    val id: Int,
    val known_for: List<KnownFor>,
    val known_for_department: String,
    val media_type: String,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val profile_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {

    override fun toString(): String {
        return if (media_type == MyConstants.MEDIA_TYPE_MOVIE) {
            title
        } else {
            name
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != this.javaClass) {
            return false
        }
        other as SearchResult
        if (id != other.id || media_type != other.media_type) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + media_type.hashCode()
        return result
    }


}