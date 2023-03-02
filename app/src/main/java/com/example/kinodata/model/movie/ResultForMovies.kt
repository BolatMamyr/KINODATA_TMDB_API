package com.example.kinodata.model.movie

data class ResultForMovies(
    // for upcoming only
    val dates: Dates,
    val page: Int,
    val results: List<RMovie>,
    val total_pages: Int,
    val total_results: Int
)