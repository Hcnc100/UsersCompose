package com.nullpointer.userscompose.core.utils

import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormat(context: Context, includeSeconds:Boolean=false): String {
    val base=if(includeSeconds)  "EEEE dd/MM/yyyy hh:mm:ss" else  "EEEE dd/MM/yyyy hh:mm"
    val newPattern = if (DateFormat.is24HourFormat(context)) base else "$base a"
    val sdf = SimpleDateFormat(newPattern, Locale.getDefault())
    return sdf.format(this)
}