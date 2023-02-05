package com.example.kinodata.model.account.accountStates

import com.google.gson.Gson
import com.google.gson.JsonElement

data class AccountStates(
    val favorite: Boolean,
    val id: Int,
    // if not rated by user it is Boolean (false), if rated then Rated JsonObject.
    val rated: JsonElement,
    val watchlist: Boolean
) {
    data class Rating(
        val value: Double
    )

    fun isRatedItemBoolean() = rated.isJsonPrimitive && rated.asJsonPrimitive.isBoolean

    fun isRatedItemRatedObject() = rated.isJsonObject

    fun ratedItemAsBoolean() = rated.asBoolean

    fun ratedItemAsRatedObject(): Rating {
        val jsonObject = rated.asJsonObject
        return Gson().fromJson(jsonObject, Rating::class.java)
    }


}