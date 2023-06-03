package com.veramed.util

import java.text.SimpleDateFormat
import java.util.*


fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}

fun convertLongToTimeScreen(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd-MM-yyyy")
    return format.format(date)
}