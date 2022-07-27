package com.nullpointer.userscompose.core.utils

import android.content.Context
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormat(context: Context, includeSeconds:Boolean=false): String {
    val base=if(includeSeconds)  "EEEE dd/MM/yyyy hh:mm:ss" else  "EEEE dd/MM/yyyy hh:mm"
    val newPattern = if (DateFormat.is24HourFormat(context)) base else "$base a"
    val sdf = SimpleDateFormat(newPattern, Locale.getDefault())
    return sdf.format(this)
}

@Composable
inline fun <reified VM : ViewModel> shareViewModel():VM {
    val activity= LocalContext.current as ComponentActivity
    return hiltViewModel(activity)
}

fun Context.getPlural(@PluralsRes stringQuality: Int, quality:Int): String {
    return resources.getQuantityString(stringQuality,quality,quality)
}