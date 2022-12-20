package com.example.kinodata.model.multiSearch

data class MultiSearch(
    val page: Int,
    val results: List<SearchResult>,
    val total_pages: Int,
    val total_results: Int
)