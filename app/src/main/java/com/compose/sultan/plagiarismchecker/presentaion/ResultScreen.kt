package com.compose.sultan.plagiarismchecker.presentaion

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.model.SimilarityWithParagraph
import com.compose.sultan.plagiarismchecker.presentaion.components.ItemSimilarity
import com.compose.sultan.plagiarismchecker.utils.Constants

@Composable
fun ResultScreen(navController: NavController, list: List<SimilarityWithParagraph>,
                 totalSimilarityBetweenFilesList:String){
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            items(list) { item ->
                ItemSimilarity(item = item)
            }

        }
        Button(
            onClick = {
                navController.popBackStack(Routes.Result.route, true)
                navController.navigate(
                    Routes.TotalResult.route,
                    Bundle().apply {
                        putString(Constants.TOTAL_SIMILARITY, totalSimilarityBetweenFilesList)
                    })
            },modifier = Modifier.align(CenterHorizontally).fillMaxWidth()
        ) {
            Text(text = "Total Result")
        }

    }

}
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    PlagiarismCheckerTheme {
//        ResultScreen(navController = null, list = listOf<SimilarityBetweenString>() , totalSimilarityBetweenFilesList ="")
//    }
//}