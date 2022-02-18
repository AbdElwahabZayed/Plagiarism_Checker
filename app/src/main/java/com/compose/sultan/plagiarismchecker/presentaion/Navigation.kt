package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.sultan.plagiarismchecker.MainActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.*
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenFiles
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.compose.sultan.plagiarismchecker.utils.Constants.ITEMS
import com.compose.sultan.plagiarismchecker.utils.Constants.TOTAL_SIMILARITY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

// It contains route names to all three screens
sealed class Routes(val route: String) {
    object Splash : Routes("splash_screen")
    object Menu : Routes("menu_screen")
    object Main : Routes("main_screen")
    object DB : Routes("db_compare")
    object Result : Routes("result_screen/{$ITEMS}/{$TOTAL_SIMILARITY}")
    object Search : Routes("search_screen/{text}")
    object TotalResult : Routes("total_result/{text}")
}

@Composable
fun Navigation(activity: MainActivity) {
    val navController = rememberNavController()
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
            DataBaseCompareScreen(activity = activity, navController)
        }

        // Search Screen
        composable(Routes.Search.route,
            arguments = listOf(navArgument("text") {
                type = NavType.StringType
            }
            )) {
            SearchScreen(
                activity = activity,
                navController,
                it.arguments?.getString("text") ?: "Null"
            )

        }

        // Result list screen
        composable(Routes.Result.route,
            arguments = listOf(navArgument(ITEMS) {
                type = NavType.StringType
            },
                navArgument(TOTAL_SIMILARITY) {
                    type = NavType.StringType
                }
            )
        ) {
            val textItems = it.arguments?.getString(ITEMS) ?: ""
            val textTotalSimilarity = it.arguments?.getString(TOTAL_SIMILARITY) ?: ""
            val gson = Gson()
            val typeItem: Type = object : TypeToken<ArrayList<SimilarityBetweenString>>() {}.type

            val items: ArrayList<SimilarityBetweenString> = gson.fromJson(textItems, typeItem)

            ResultScreen(
                navController,
                items,
                textTotalSimilarity
            )
        }
        composable(Routes.TotalResult.route,
            arguments = listOf(
                navArgument(TOTAL_SIMILARITY) {
                    type = NavType.StringType
                }
            )
        ) {
            val gson = Gson()
            val textTotalSimilarity = it.arguments?.getString(TOTAL_SIMILARITY) ?: ""
            val typeTotalSimilarity: Type =
                object : TypeToken<ArrayList<SimilarityBetweenFiles>>() {}.type
            val totalSimilarityBetweenFilesList: ArrayList<SimilarityBetweenFiles> =
                gson.fromJson(textTotalSimilarity, typeTotalSimilarity)
            TotalResultScreen(navController = navController,totalSimilarityBetweenFilesList)
        }
    }
}


fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}

fun NavController.popBackStack(
    route: String,
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        popBackStack(id, true)
    }
}