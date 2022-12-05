package com.example.kinodata.model.multiSearch

data class multiSearch(
    val page: Int,
    val searchResults: List<SearchResult>,
    val total_pages: Int,
    val total_results: Int
)