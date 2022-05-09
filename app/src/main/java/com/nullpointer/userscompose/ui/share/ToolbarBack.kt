package com.nullpointer.userscompose.ui.share


import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nullpointer.userscompose.R
import timber.log.Timber


@Composable
fun ToolbarBack(title: String, actionBack: (() -> Unit)? = null) {
    TopAppBar(title = { Text(title) },
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
fun SelectionMenuToolbar(
    titleDefault: String,
    titleSelection: String,
    numberSelection: Int,
    actionClear: () -> Unit,
    deleterAll: () -> Unit,
) {
    val (showMenu, changeVisibleMenu) = rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = if (numberSelection == 0) MaterialTheme.colors.primarySurface else MaterialTheme.colors.primary,
        title = {
            Text(if (numberSelection == 0) titleDefault else titleSelection)
        },
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