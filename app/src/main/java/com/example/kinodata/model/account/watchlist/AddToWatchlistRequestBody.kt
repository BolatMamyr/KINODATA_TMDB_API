package com.example.kinodata.model.account.watchlist

data class AddToWatchlistRequestBody(
    val media_id: Int,
    val media_type: String,
    val watchlist: Boolean
)