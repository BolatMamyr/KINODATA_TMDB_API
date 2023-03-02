package com.example.kinodata.model.collection

import com.example.kinodata.model.movie.RMovie

data class CollectionResult(
    val backdrop_path: String,
    val id: Int,
    val name: String,
    val overview: String,
    val parts: List<RMovie>,
    val poster_path: String
)