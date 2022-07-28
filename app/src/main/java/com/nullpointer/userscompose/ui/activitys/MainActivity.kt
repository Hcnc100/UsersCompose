package com.nullpointer.userscompose.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.nullpointer.userscompose.ui.screens.NavGraphs
import com.nullpointer.userscompose.ui.theme.UsersComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isSplash=true
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{ isSplash }
        }
        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.IO){
                delay(1500)
                isSplash=false
            }
        }
        setContent {
            UsersComposeTheme {
                DestinationsNavHost(
                    navController = rememberNavController(),
                    navGraph = NavGraphs.root,
                )
            }
        }
    }
}

