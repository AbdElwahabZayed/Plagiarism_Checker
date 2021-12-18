package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.Text

// It contains route names to all three screens
sealed class Routes(val route: String) {
    object Splash : Routes("splash_screen")
    object Menu : Routes("menu_screen")
    object Main : Routes("main_screen")
    object DB : Routes("db_compare")
    object Search : Routes("search_screen")
}
@Composable
fun Navigation(activity: MainActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }


        composable(Routes.Menu.route){
            MenuScreen(activity = activity, navController = navController)
        }

        // Main Screen
        composable(Routes.Main.route) {
            MainScreen(activity = activity)
        }
        // Main Screen
        composable(Routes.DB.route) {
            DataBaseCompareScreen(activity = activity, navController)
        }

        // Main Screen
        composable(Routes.Search.route) {
                    SearchScreen(
                        activity = activity,
                        navController,
                        it.arguments?.getParcelable("text")?:Text("")
                    )
                }

    }
}