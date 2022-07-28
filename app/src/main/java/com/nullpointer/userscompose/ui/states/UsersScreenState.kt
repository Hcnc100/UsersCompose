package com.nullpointer.userscompose.ui.states

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class UsersScreenState(
    val scaffoldState: ScaffoldState,
    val context: Context,
    val lazyListState: LazyGridState,
) {

     val isScrollInProgress get() = lazyListState.isScrollInProgress

    suspend fun showSnackMessage(@StringRes stringRes: Int) {
        scaffoldState.snackbarHostState.showSnackbar(
            context.getString(stringRes)
        )
    }

}

@Composable
fun rememberUsersScreenState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lazyListState: LazyGridState= rememberLazyGridState(),
    context: Context = LocalContext.current,
) = remember(scaffoldState) {
    UsersScreenState(scaffoldState, context, lazyListState)
}