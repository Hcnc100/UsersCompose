package com.nullpointer.userscompose.ui.share


import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.utils.getPlural


@Composable
fun ToolbarBack(title: String, actionBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        navigationIcon = {
            actionBack?.let { action ->
                IconButton(onClick = { action() }) {
                    Icon(painterResource(id = R.drawable.ic_arrow_back),
                        stringResource(id = R.string.description_arrow_back))
                }
            }
        })
}

@Composable
fun SelectToolbar(
    @StringRes
    titleDefault: Int,
    @PluralsRes
    titleSelection: Int,
    numberSelection: Int,
    actionClear: () -> Unit,
    deleterAll: () -> Unit,
    context: Context = LocalContext.current
) {

    val (showMenu, changeVisibleMenu) = rememberSaveable { mutableStateOf(false) }
    val title by remember {
        derivedStateOf {
            if (numberSelection == 0)
                context.getString(titleDefault) else
                context.getPlural(titleSelection, numberSelection)
        }
    }


    TopAppBar(
        backgroundColor = if (numberSelection == 0)  MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
        title = { Text(title) },
        contentColor = Color.White,
        actions = {
            if (numberSelection != 0) {
                IconButton(onClick = actionClear) {
                    Icon(painterResource(id = R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.description_clear_selection))
                }
            } else {
                IconButton(onClick = { changeVisibleMenu(!showMenu) }) {
                    Icon(painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.description_icon_options))
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { changeVisibleMenu(false) }
                ) {
                    DropdownMenuItem(onClick = {
                        deleterAll()
                        changeVisibleMenu(false)
                    }) {
                        Text(text = stringResource(R.string.deleter_all))
                    }
                }
            }
        }
    )
}

