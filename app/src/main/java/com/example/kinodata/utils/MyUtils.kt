package com.example.kinodata.utils

import android.view.View
import com.example.kinodata.R

class MyUtils {

    companion object {

        fun getRatingColorId(rating: Double, view: View): Int {
            return if (rating == .0) {
                view.resources.getColor(R.color.light_gray, null)
            } else if (rating < 5.0) {
                view.resources.getColor(R.color.red, null)
            } else if (rating < 7.0) {
                view.resources.getColor(R.color.light_gray, null)
            } else  {
                view.resources.getColor(R.color.green, null)
            }
        }
        fun getShortenedString(string: String): String {
            return if (string.length > 19) {
                string.substring(0, 19) + "..."
            } else string
        }

        fun getFormattedDate(date: String, view: View): String {
            return if(date.length > 7) {
                val year = date.substring(0, 4)
                var month = date.substring(5, 7)
                var day = date.substring(8)
                if (day[0] == '0') day = day[1].toString()

                month = when(month) {
                    "01" -> view.resources.getString(R.string.january)
                    "02" -> view.resources.getString(R.string.february)
                    "03" -> view.resources.getString(R.string.march)
                    "04" -> view.resources.getString(R.string.april)
                    "05" -> view.resources.getString(R.string.may)
                    "06" -> view.resources.getString(R.string.june)
                    "07" -> view.resources.getString(R.string.july)
                    "08" -> view.resources.getString(R.string.august)
                    "09" -> view.resources.getString(R.string.september)
                    "10" -> view.resources.getString(R.string.october)
                    "11" -> view.resources.getString(R.string.november)
                    "12" -> view.resources.getString(R.string.december)
                    else -> ""
                }
                "$day $month, $year"
            } else ""
        }
    }
}