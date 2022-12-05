package com.example.kinodata.model.tv

data class ResultForTvSeries(
    val page: Int,
    val results: List<RTvSeries>,
    val total_pages: Int,
    val total_results: Int
)