package com.example.kinodata.model.auth.requestBodies

data class ValidateTokenRequestBody(
    val username: String,
    val password: String,
    val request_token: String
)
