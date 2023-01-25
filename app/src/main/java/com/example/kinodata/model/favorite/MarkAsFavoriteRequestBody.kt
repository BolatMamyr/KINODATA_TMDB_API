package com.example.kinodata.model.favorite

data class MarkAsFavoriteRequestBody(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String
)