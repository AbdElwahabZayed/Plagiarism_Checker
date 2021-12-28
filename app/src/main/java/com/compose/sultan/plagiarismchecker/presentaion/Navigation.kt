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
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.util.ArrayList

// It contains route names to all three screens
sealed class Routes(val route: String) {
    object Splash : Routes("splash_screen")
    object Menu : Routes("menu_screen")
    object Main : Routes("main_screen")
    object DB : Routes("db_compare")
    object ResultResult : Routes("result_screen")
    object Search : Routes("search_screen/{text}")
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
        // Main Screen
        composable(Routes.DB.route) {
            DataBaseCompareScreen(activity = activity, navController)
        }

        // Main Screen
        composable(Routes.Search.route,
            arguments = listOf(navArgument("text") {
                type = NavType.StringType
            }
            )) {
            SearchScreen(
                activity = activity,
                navController,
                it.arguments?.getString("text") ?: "www"
            )

        }

        // Result list screen
        composable(Routes.ResultResult.route,
            arguments = listOf(navArgument("items") {
                type = NavType.StringType
            }
            )
        ) {
            val x = it.arguments?.getString("items") ?: ""
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                ArrayList::class.java,
                SimilarityBetweenString::class.java
            )
            val adapter = moshi.adapter<ArrayList<SimilarityBetweenString>>(type)

            ResultScreen(
                navController,
                adapter.fromJson(x)?: listOf()
            )
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