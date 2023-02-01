package com.example.kinodata.model.account.favorite

data class AddToFavoriteRequestBody(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String
)