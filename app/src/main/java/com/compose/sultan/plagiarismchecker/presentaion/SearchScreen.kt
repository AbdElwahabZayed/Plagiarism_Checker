package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen.DataBaseCompareViewModel
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: DataBaseCompareViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        GlideImage(
            imageModel = R.drawable.search,
            contentDescription = "Search",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
    LaunchedEffect(Unit) {
        viewModel.compare {
            navController.popBackStack(Routes.Search.route, true)
            navController.navigate(Routes.Result.route)
        }
    }
}


