package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.sultan.plagiarismchecker.MainActivity

@Composable
fun Navigation(activity: MainActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = "splash_screen") {
        composable("splash_screen",) {
            SplashScreen(navController = navController)
        }


        // Main Screen
        composable("main_screen") {
            MainScreen(activity = activity)
        }
    }
}