package com.example.kinodata.model.account.accountStates

import com.google.gson.Gson
import com.google.gson.JsonElement

data class AccountStates(
    val favorite: Boolean,
    val id: Int,
    val rated: JsonElement,
    val watchlist: Boolean
) {
    data class Rated(
        val value: Double
    )

    fun isRatedItemBoolean() = rated.isJsonPrimitive && rated.asJsonPrimitive.isBoolean

    fun isRatedItemRatedObject() = rated.isJsonObject

    fun ratedItemAsBoolean() = rated.asBoolean

    fun ratedItemAsRatedObject(): Rated {
        val jsonObject = rated.asJsonObject
        return Gson().fromJson(jsonObject, Rated::class.java)
    }


}