package com.example.kinodata.model.account.accountDetails

data class AccountDetails(
    val avatar: Avatar,
    val id: Int,
    val include_adult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
) {
    fun getAvatarPath(): String? {
        return avatar.tmdb.avatar_path
    }
}