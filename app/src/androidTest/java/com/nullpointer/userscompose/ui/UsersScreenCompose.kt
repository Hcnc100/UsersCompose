package com.nullpointer.userscompose.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_ADD
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_CANCEL
import com.nullpointer.userscompose.core.constants.Constants.TAG_LIST_USERS
import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.screens.users.UsersScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UsersScreenCompose {

    private class FakeUsersRepo(
        listUsersFake: List<User> = emptyList(),
        val errorRequestNewUser: Exception? = null
    ) : UsersRepository {

        private val _listUsers = MutableStateFlow(listUsersFake)
        override val listUsers: Flow<List<User>> = _listUsers.asStateFlow()

        override suspend fun addNewUser(): User {
            delay(800)
            errorRequestNewUser?.let { throw it }
            var randomUser = User.getUserRandom()
            while (_listUsers.value.contains(randomUser)) {
                randomUser = User.getUserRandom()
            }
            _listUsers.value = _listUsers.value + randomUser
            return randomUser
        }

        override suspend fun deleterUser(user: User) {
            val currentList = _listUsers.value
            if (currentList.contains(user)) {
                _listUsers.value = currentList - user
            } else {
                throw RuntimeException("User not found")
            }
        }

        override suspend fun deleterAllUsers() {
            _listUsers.value = emptyList()
        }

        override suspend fun deleterUserByIds(list: List<Long>) {
            val listDeleterUsers = _listUsers.value.filter {
                list.contains(it.id)
            }
            _listUsers.value = _listUsers.value - listDeleterUsers.toSet()
        }

        override suspend fun getUserById(id: Long): User? {
            return _listUsers.value.find { it.id == id }
        }

    }

    @get:Rule
    var composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun visibilityButtonsActions() {
        val userViewModel = UsersViewModel(FakeUsersRepo())
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }

        // * test only exist button add
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).assertExists()
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
        // * test hidden button add when processing data and show cancel operation
        composeTestRule.waitUntil { userViewModel.isProcessing }
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_BUTTON_CANCEL).assertExists()
        composeTestRule.waitUntil { !userViewModel.isProcessing }
        // * test show button add when processing finish
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).assertExists()
    }

    @Test
    fun showMessageWhenCancelOperationAddNewUserAndRestoreInterface() {
        val userViewModel = UsersViewModel(FakeUsersRepo())
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }
        // * click init add for request new user
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
        // * await to processing
        composeTestRule.waitUntil { userViewModel.isProcessing }
        // * cancel operation
        composeTestRule.onNodeWithTag(TAG_BUTTON_CANCEL).performClick()
        // * show message cancel operation
        composeTestRule.onNodeWithText(context.getString(R.string.action_stop_add_user))
            .assertExists()
        composeTestRule.waitUntil { !userViewModel.isProcessing }
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).assertExists()
    }

    @Test
    fun showMessageNoNetwork() {
        val userViewModel =
            UsersViewModel(FakeUsersRepo(errorRequestNewUser = NullPointerException()))
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }
        // * click init add for request new user
        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
        composeTestRule.waitUntil { userViewModel.isProcessing }
        composeTestRule.waitUntil { !userViewModel.isProcessing }
        composeTestRule.onNodeWithText(context.getString(R.string.server_time_now)).assertExists()
    }

    @Test
    fun showCorrectNumberUsers() {
        val userViewModel = UsersViewModel(FakeUsersRepo())
        val randomTest = (5..15).random()
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }
        repeat(randomTest) {
            composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
            composeTestRule.waitUntil { !userViewModel.isProcessing }
            composeTestRule.onNodeWithText(
                context.getString(
                    R.string.title_numbers_user_saved,
                    it + 1
                )
            ).assertExists()
        }
    }

    @Test
    fun showCorrectAllUsers() {
        val listFakeUsers = User.generateListUsers(100)
        val userViewModel = UsersViewModel(FakeUsersRepo(listFakeUsers))
        val randomTest = (5..listFakeUsers.size).random()
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }
        with(composeTestRule) {
            repeat(randomTest) {
                val randomUser = listFakeUsers.random()
                onNodeWithTag(TAG_LIST_USERS).performScrollToKey(randomUser.id)
                onNodeWithText(randomUser.name).assertExists()
                onNodeWithContentDescription(
                    context.getString(
                        R.string.description_img_user,
                        randomUser.name
                    )
                ).assertExists()
            }
        }

    }


//    @Test
//    fun selectUsers() = runBlocking {
//        val userList = User.generateListUsers(100)
//        val userFake = FakeUsersRepo(userList)
//        val userViewModel = UsersViewModel(userFake)
//        val selectViewModel = SelectViewModel(SavedStateHandle())
//        val expectedList = mutableListOf<User>()
//        composeTestRule.setContent {
//            UsersScreen(userViewModel, selectViewModel)
//        }
//
//        with(composeTestRule) {
//            repeat(2) {
//                val user = userList.random()
//
//                if (!expectedList.contains(user)) {
//                    onNodeWithTag(TAG_LIST_USERS).performScrollToKey(user.id)
//
//                    onNodeWithText(user.name).printToLog("user ${user.id}")
//                    onNodeWithText(user.name).performSemanticsAction(SemanticsActions.OnLongClick)
//
//                    expectedList.add(user)
//                    delay(1000)
//                }
//            }
//        }
//        assert(expectedList.size == selectViewModel.numberSelection)
//    }
}