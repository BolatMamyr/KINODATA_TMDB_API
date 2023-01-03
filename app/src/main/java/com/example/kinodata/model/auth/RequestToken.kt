package com.example.kinodata.model.auth

data class RequestToken(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)