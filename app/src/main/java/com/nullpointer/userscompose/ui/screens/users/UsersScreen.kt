package com.nullpointer.userscompose.ui.screens.users


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_CANCEL
import com.nullpointer.userscompose.core.constants.Constants.TAG_LIST_USERS
import com.nullpointer.userscompose.core.states.Resource
import com.nullpointer.userscompose.core.utils.shareViewModel
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.presentation.SelectViewModel
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.screens.destinations.DetailsScreenDestination
import com.nullpointer.userscompose.ui.screens.empty.EmptyScreen
import com.nullpointer.userscompose.ui.screens.users.components.LoadingUsers
import com.nullpointer.userscompose.ui.screens.users.components.UserItem
import com.nullpointer.userscompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.userscompose.ui.share.FabAnimation
import com.nullpointer.userscompose.ui.share.SelectToolbar
import com.nullpointer.userscompose.ui.states.UsersScreenState
import com.nullpointer.userscompose.ui.states.rememberUsersScreenState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.first


@Destination(start = true)
@Composable
fun UsersScreen(
    userViewModel: UsersViewModel = shareViewModel(),
    selectViewModel: SelectViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null,
    usersScreenState: UsersScreenState = rememberUsersScreenState()
) {
    val stateListUsers by userViewModel.listUsers.collectAsState()

    LaunchedEffect(key1 = Unit) {
        userViewModel.messageErrorProcess.collect(usersScreenState::showSnackMessage)
    }

    LaunchedEffect(key1 = Unit) {
        userViewModel.listUsers.first { it is Resource.Success }.let {
            val firstUsers = it as Resource.Success
            if (firstUsers.data.isNotEmpty()) {
                selectViewModel.restoreSelectUsers(firstUsers.data)
            }
        }
    }

    BackHandler(selectViewModel.isSelectedEnable, onBack = selectViewModel::clearSelection)
    Scaffold(
        scaffoldState = usersScreenState.scaffoldState,
        topBar = {
            SelectToolbar(
                titleDefault = R.string.app_name,
                titleSelection = R.plurals.selected_items,
                numberSelection = selectViewModel.numberSelection,
                actionClear = selectViewModel::clearSelection,
                deleterAll = userViewModel::deleterAllUsers)
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
            ButtonToggleAddRemove(isVisible = !userViewModel.isProcessing && !usersScreenState.isScrollInProgress,
                isSelectedEnable = selectViewModel.isSelectedEnable,
                descriptionButtonAdd = stringResource(R.string.description_add_mew_user),
                actionAdd = userViewModel::addNewUser,
                descriptionButtonRemove = stringResource(R.string.description_deleter_user_selected),
                actionRemove = {
                    userViewModel.deleterUser(selectViewModel.getListSelectionAndClear())
                })
        }
    ) { padding ->

        when (val listUsers = stateListUsers) {
            Resource.Failure -> {
                EmptyScreen(
                    animation = R.raw.empty,
                    textEmpty = stringResource(R.string.text_empty_users),
                    modifier = Modifier.padding(padding)
                )
            }
            Resource.Loading -> {
                LoadingUsers(modifier = Modifier.padding(padding))
            }
            is Resource.Success -> {
                if (listUsers.data.isEmpty()) {
                    EmptyScreen(
                        animation = R.raw.empty,
                        textEmpty = stringResource(R.string.text_empty_users),
                        modifier = Modifier.padding(padding)
                    )
                } else {
                    ListUsers(
                        modifier = Modifier.padding(padding),
                        listUsers = listUsers.data,
                        gridState = usersScreenState.lazyListState,
                        isSelectedEnable = selectViewModel.isSelectedEnable,
                        changeItemSelected = selectViewModel::changeItemSelected,
                        actionDetails = { navigator?.navigate(DetailsScreenDestination(it)) }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListUsers(
    modifier: Modifier = Modifier,
    listUsers: List<User>,
    gridState: LazyGridState,
    isSelectedEnable: Boolean,
    changeItemSelected: (User) -> Unit,
    actionDetails: (User) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = gridState,
        modifier = modifier.testTag(TAG_LIST_USERS)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }, key = "Number of users") {
            Text(
                text = stringResource(R.string.title_numbers_user_saved, listUsers.size),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                style = MaterialTheme.typography.h6
            )
        }


        items(listUsers, key = { it.id }) { user ->
            UserItem(
                user = user,
                isSelectedEnable = isSelectedEnable,
                changeSelectState = changeItemSelected,
                actionClickSimple = actionDetails,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
