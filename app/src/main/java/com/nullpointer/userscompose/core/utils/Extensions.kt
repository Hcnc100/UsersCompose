package com.nullpointer.userscompose.core.utils

import android.content.Context
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.annotation.PluralsRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.*
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

fun ViewModel.launchSafeIO(
    blockBefore: suspend CoroutineScope.() -> Unit = {},
    blockAfter: suspend CoroutineScope.() -> Unit = {},
    blockException: suspend CoroutineScope.(Exception) -> Unit = {},
    blockIO: suspend CoroutineScope.() -> Unit,
): Job {
    return viewModelScope.launch {
        try {
            blockBefore()
            withContext(Dispatchers.IO) { blockIO() }
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> throw e
                else -> blockException(e)
            }
        } finally {
            blockAfter()
        }
    }
}

val AsyncImagePainter.isSuccess get() = state is AsyncImagePainter.State.Success

@Composable
fun getGrayColor(): Color {
    return if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
}

fun Modifier.myShimmer(
    shimmer: Shimmer,
): Modifier = composed {
    shimmer(shimmer).background(getGrayColor())
}
