package com.example.kinodata.model.favorite

data class AddOrRemoveFromFavoriteRequestBody(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String
)