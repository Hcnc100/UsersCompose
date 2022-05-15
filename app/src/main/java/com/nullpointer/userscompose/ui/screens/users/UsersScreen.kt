package com.nullpointer.userscompose.ui.screens.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_ADD
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_CANCEL
import com.nullpointer.userscompose.core.constants.Constants.TAG_LIST_USERS
import com.nullpointer.userscompose.presentation.SelectViewModel
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.screens.destinations.DetailsScreenDestination
import com.nullpointer.userscompose.ui.screens.empty.EmptyScreen
import com.nullpointer.userscompose.ui.screens.users.components.UserItem
import com.nullpointer.userscompose.ui.share.BackHandler
import com.nullpointer.userscompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.userscompose.ui.share.FabAnimation
import com.nullpointer.userscompose.ui.share.SelectionMenuToolbar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first


@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun UsersScreen(
    userViewModel: UsersViewModel,
    selectViewModel: SelectViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null,
) {

    val listState = rememberLazyListState()
    val stateListUsers = userViewModel.listUsers.collectAsState()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val messageUser = userViewModel.messageErrorProcess

    LaunchedEffect(key1 = Unit) {
        messageUser.filter { it != -1 }.collect {
            scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(it),
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        userViewModel.listUsers.first { it != null }.let {
            if (!it.isNullOrEmpty()) {
                selectViewModel.restoreSelectUsers(it)
            }
        }
    }

    BackHandler(selectViewModel.isSelectedEnable) {
        selectViewModel.clearSelection()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                SelectionMenuToolbar(
                    titleDefault = stringResource(id = R.string.app_name),
                    numberSelection = selectViewModel.numberSelection,
                    actionClear = selectViewModel::clearSelection,
                    deleterAll = userViewModel::deleterAllUsers,
                    titleSelection = context.resources.getQuantityString(
                        R.plurals.selected_items,
                        selectViewModel.numberSelection,
                        selectViewModel.numberSelection),
                )
            }
        },
        floatingActionButton = {
            FabAnimation(isVisible = userViewModel.isProcessing,
                Icons.Default.Clear,
                stringResource(R.string.description_cancel_add_user),
                isProgress = true,
                backgroundColor = Color.Gray,
                action = userViewModel::cancelAddNewUser,
                modifier = Modifier.testTag(TAG_BUTTON_CANCEL)
            )
            ButtonToggleAddRemove(isVisible = !userViewModel.isProcessing && !listState.isScrollInProgress,
                isSelectedEnable = selectViewModel.isSelectedEnable,
                descriptionButtonAdd = stringResource(R.string.description_add_mew_user),
                actionAdd = userViewModel::addNewUser,
                descriptionButtonRemove = stringResource(R.string.description_deleter_user_selected),
                actionRemove = {
                    userViewModel.deleterUser(selectViewModel.getListSelectionAndClear())
                })
        }
    ) {
        val listUsers = stateListUsers.value
        when {
            listUsers == null -> Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            listUsers.isEmpty() -> EmptyScreen(animation = R.raw.empty,
                textEmpty = stringResource(R.string.text_empty_users))
            else -> LazyVerticalGrid(cells = GridCells.Adaptive(150.dp),
                state = listState,
                modifier = Modifier.testTag(
                    TAG_LIST_USERS
                )) {
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Text(text = stringResource(R.string.title_numbers_user_saved, listUsers.size),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.h6)
                }


                items(listUsers.size) { index ->
                    UserItem(
                        user = listUsers[index],
                        isSelectedEnable = selectViewModel.isSelectedEnable,
                        changeSelectState = selectViewModel::changeItemSelected,
                        actionClickSimple = {
                            navigator?.navigate(DetailsScreenDestination.invoke(listUsers[index]))
                        })
                }
            }
        }
    }
}