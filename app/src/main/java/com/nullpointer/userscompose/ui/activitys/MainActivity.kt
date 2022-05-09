package com.nullpointer.userscompose.ui.activitys

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.nullpointer.userscompose.presentation.UsersViewModel
import com.nullpointer.userscompose.ui.screens.NavGraphs
import com.nullpointer.userscompose.ui.theme.UsersComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UsersViewModel>()
    private var loading = true

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("KEY_LOADING", loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        loading = savedInstanceState?.getBoolean("KEY_LOADING") ?: true
        // ! comment for unit test
        // splash.setKeepOnScreenCondition { loading }
        setContent {
            UsersComposeTheme {
                val navController = rememberNavController()
                LaunchedEffect(key1 = Unit) {
                    userViewModel.listUsers.takeWhile { loading }.collect {
                        delay(200)
                        loading = it != null
                    }
                }
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    dependenciesContainerBuilder = {
                        dependency(userViewModel)
                    }
                )
            }
        }
    }
}

