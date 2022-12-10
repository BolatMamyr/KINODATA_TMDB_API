package com.example.kinodata.model.credit.person.personTvSeries


data class PersonTvSeries(
    val adult: Boolean,
    val backdrop_path: String,
    val character: String,
    val credit_id: String,
    val episode_count: Int,
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
        other as PersonTvSeries
        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id
        result = 31 * result + overview.hashCode()
        result = 31 * result + character.hashCode()
        return result
    }
}