package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.presentaion.components.ItemSimilarity
import com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen.DataBaseCompareViewModel

@Composable
fun ResultScreen(
    navController: NavController, viewModel: DataBaseCompareViewModel
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            items(viewModel.similarityWithParagraphs) { item ->
                ItemSimilarity(item = item)
            }
        }
        Button(
            onClick = {
                navController.popBackStack(Routes.Result.route, true)
                navController.navigate(Routes.TotalResult.route)
            }, modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(text = "Total Result")
        }

    }
}