package com.example.kinodata.model.tv.tvDetails

import android.content.Context
import android.util.Log
import com.example.kinodata.R

data class TvDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val created_by: List<CreatedBy>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val last_episode_to_air: LastEpisodeToAir,
    val name: String,
    val networks: List<Network>,
    val next_episode_to_air: Any,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
) {
    fun getGenres(): String {
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

    fun getCountries(context: Context): String {
        val list = production_countries.map { it.name }
        var result = ""
        for ((index, s) in list.withIndex()) {
            val country = if (s == context.getString(R.string.united_states_of_america)) {
                context.getString(R.string.usa)
            } else {
                s
            }
            if (index == 0) {
                result += s
            } else {
                result += ", $country"
            }
        }
        return result
    }
}