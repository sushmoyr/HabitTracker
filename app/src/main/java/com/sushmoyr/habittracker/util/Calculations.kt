package com.sushmoyr.habittracker.util

import android.annotation.SuppressLint
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Calculations {

    @SuppressLint("SimpleDateFormat")
    fun calculateTimeBetweenDates(startDate: String): String {

        val endDate = timeStampToString(System.currentTimeMillis())

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date1 = sdf.parse(startDate)
        val date2 = sdf.parse(endDate)

        var isNegative = false

        var difference = date2.time - date1.time
        if (difference < 0) {
            difference = -(difference)
            isNegative = true

        }

        val minutes = difference / 60 / 1000
        val hours = difference / 60 / 1000 / 60
        val days = (difference / 60 / 1000 / 60) / 24
        val months = (difference / 60 / 1000 / 60) / 24 / (365 / 12)
        val years = difference / 60 / 1000 / 60 / 24 / 365

        if (isNegative) {

            return when {
                minutes < 60 -> "Starts in $minutes minutes"
                hours == 1L -> "Starts in an hour"
                hours < 24 -> "Starts in $hours hours"
                days == 1L -> "Starts in a day"
                days < 30 -> "Starts in $days days"
                months == 1L -> "Starts in a mont"
                months < 12 -> "Starts in $months months"
                else -> "Starts in $years years"
            }
        }

        return when {
            minutes < 60 -> "$minutes minutes ago"
            hours == 1L -> "Started in an hour"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "Started in a day"
            days < 30 -> "$days days ago"
            months == 1L -> "Started in a mont"
            months < 12 -> "$months months ago"
            else -> "$years years ago"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun timeStampToString(timeStamp: Long): String {

        val stamp = Timestamp(timeStamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = sdf.format(Date(stamp.time))

        return date.toString()

    }

    fun cleanDate(_day: Int, _month: Int, _year: Int): String {
        var day = _day.toString()
        var month = _month.toString()

        if (_day < 10) {
            day = "0$_day"
        }
        if (_month < 9) { //Because the month instance we retrieve starts at 0 and it's stupid!
            month = "0${_month+1}"
        }

        return "$day/$month/$_year"
    }

    fun cleanTime(_hour: Int, _minute: Int): String {
        var hour = _hour.toString()
        var minute = _minute.toString()

        if (_hour < 10) {
            hour = "0$_hour"
        }
        if (_minute < 10) {
            minute = "0$_minute"
        }
        return "$hour:$minute"
    }
}