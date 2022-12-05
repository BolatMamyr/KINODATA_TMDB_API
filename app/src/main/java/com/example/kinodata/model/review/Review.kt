package com.example.kinodata.model.review

import android.content.Context
import android.os.Parcelable
import com.example.kinodata.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Review(
    val author: String,
    val author_details: @RawValue AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
) : Parcelable {

    fun getDate(context: Context): String {
        return if (created_at.length > 9) {
            val year = created_at.substring(0, 4)
            var month = created_at.substring(5, 7)
            var day = created_at.substring(8, 10)
            if (day[0] == '0') day = day[1].toString()

            month = when (month) {
                "01" -> context.resources.getString(R.string.january)
                "02" -> context.resources.getString(R.string.february)
                "03" -> context.resources.getString(R.string.march)
                "04" -> context.resources.getString(R.string.april)
                "05" -> context.resources.getString(R.string.may)
                "06" -> context.resources.getString(R.string.june)
                "07" -> context.resources.getString(R.string.july)
                "08" -> context.resources.getString(R.string.august)
                "09" -> context.resources.getString(R.string.september)
                "10" -> context.resources.getString(R.string.october)
                "11" -> context.resources.getString(R.string.november)
                "12" -> context.resources.getString(R.string.december)
                else -> ""
            }
            "$day $month, $year"
        } else ""

    }

    override fun equals(other: Any?): Boolean {
        if(other?.javaClass != javaClass) return false
        other as Review

        return id == other.id && author == other.author && content == other.content
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}

