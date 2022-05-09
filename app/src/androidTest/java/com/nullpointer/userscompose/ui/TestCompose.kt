package com.nullpointer.userscompose.ui

import android.content.Context
import android.util.Log
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_ADD
import com.nullpointer.userscompose.core.constants.Constants.TAG_BUTTON_CANCEL
import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.presentation.SelectViewModel
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.activitys.MainActivity
import com.nullpointer.userscompose.ui.screens.users.UsersScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.lang.Thread.sleep
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalCoilApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestCompose {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var userRepo: UsersRepository

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun init() {
        hiltTestRule.inject()
    }

    @Test
    fun visibilityButtonsActions() {
        val userViewModel = UsersViewModel(userRepo)
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
        val userViewModel = UsersViewModel(userRepo)
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
    fun showUsersCorrectlyWhenAddAndChangeTitle() {
        val userViewModel = UsersViewModel(userRepo)
        val randomTest = (5..15).random()
        composeTestRule.setContent {
            UsersScreen(userViewModel)
        }
        repeat(randomTest) {
            composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
            composeTestRule.onNodeWithTag(TAG_BUTTON_CANCEL).assertExists()
            composeTestRule.waitUntil { !userViewModel.isProcessing }
            composeTestRule.onNodeWithText("Name $it").assertExists()
            composeTestRule.onNodeWithText(context.getString(R.string.title_numbers_user_saved,
                it + 1)).assertExists()
        }
    }

    // ! i need smooth scroll for see this
//    @OptIn(ExperimentalTestApi::class)
//    @Test
//    fun hiddenButtonAddWhenScrollListUsers() {
//        val userViewModel = UsersViewModel(userRepo)
//        val randomTest = (10..22).random()
//        composeTestRule.setContent {
//            UsersScreen(userViewModel)
//        }
//        repeat(randomTest) {
//            composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
//            composeTestRule.waitUntil(1500) { !userViewModel.isProcessing }
//        }
//        val node=composeTestRule.onAllNodes(hasScrollAction()).onFirst()
//        node.performScrollToIndex(randomTest)
//        composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).assertDoesNotExist()
//    }


//    @Test
//     fun selectUsers() {
//        val userViewModel = UsersViewModel(userRepo)
//        val selectViewModel = SelectViewModel(SavedStateHandle())
//        val listPreSelectUsers = mutableListOf<SemanticsNodeInteraction>()
//        val randomTest = (5..10).random()
//        composeTestRule.setContent {
//            UsersScreen(userViewModel, selectViewModel)
//        }
//        repeat(randomTest) {
//            composeTestRule.onNodeWithTag(TAG_BUTTON_ADD).performClick()
//            composeTestRule.waitUntil(1500) { !userViewModel.isProcessing }
//            composeTestRule.onNodeWithText("Name $it").assertExists()
//            if (Random.nextBoolean()) listPreSelectUsers.add(composeTestRule.onNodeWithText("Name $it"))
//        }
//        composeTestRule.mainClock.autoAdvance=false
//        listPreSelectUsers.forEach{
//            it.performSemanticsAction(SemanticsActions.OnLongClick)
//            composeTestRule.mainClock.advanceTimeBy(500)
//        }
//
//        Log.d("TAG","Select list ${listPreSelectUsers.size}")
//        Log.d("TAG","Select view model ${selectViewModel.numberSelection}")
//        assert(selectViewModel.numberSelection == listPreSelectUsers.size)
//    }
}