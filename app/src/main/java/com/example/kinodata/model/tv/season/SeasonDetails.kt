package com.example.kinodata.model.tv.season

data class SeasonDetails(
    val _id: String,
    val air_date: String?,
    val episodes: List<Episode>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val season_number: Int
)