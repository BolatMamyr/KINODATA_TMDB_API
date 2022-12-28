package com.example.kinodata.model.persons.popular

data class ResultForPopularPersons(
    val page: Int,
    val results: List<PopularPerson>,
    val total_pages: Int,
    val total_results: Int
)