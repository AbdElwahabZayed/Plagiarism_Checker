package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen.DataBaseCompareScreen
import com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen.DataBaseCompareViewModel
import com.compose.sultan.plagiarismchecker.presentaion.main.MainScreen
import com.compose.sultan.plagiarismchecker.utils.Constants.ITEMS
import com.compose.sultan.plagiarismchecker.utils.Constants.TOTAL_SIMILARITY

// It contains route names to all three screens
sealed class Routes(val route: String) {
    data object Splash : Routes("splash_screen")
    data object Menu : Routes("menu_screen")
    data object Main : Routes("main_screen")
    data object DB : Routes("db_compare")
    data object Result : Routes("result_screen/{$ITEMS}/{$TOTAL_SIMILARITY}")
    data object Search : Routes("search_screen/{text}")
    data object TotalResult : Routes("total_result/{$TOTAL_SIMILARITY}")
}

@Composable
fun Navigation(activity: MainActivity) {
    val navController = rememberNavController()
    val dataBaseCompareViewModel: DataBaseCompareViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }


        composable(Routes.Menu.route) {
            MenuScreen(activity = activity, navController = navController)
        }

        // Main Screen
        composable(Routes.Main.route) {
            MainScreen(activity = activity)
        }
        // DB Screen
        composable(Routes.DB.route) {
            DataBaseCompareScreen(activity = activity, navController, dataBaseCompareViewModel)
        }

        // Search Screen
        composable(Routes.Search.route) {
            SearchScreen(
                navController,
                dataBaseCompareViewModel
            )
        }

        // Result list screen
        composable(Routes.Result.route) {
            ResultScreen(
                navController,
                dataBaseCompareViewModel
            )
        }
        composable(Routes.TotalResult.route) {
            TotalResultScreen(navController = navController, dataBaseCompareViewModel)
        }
    }
}