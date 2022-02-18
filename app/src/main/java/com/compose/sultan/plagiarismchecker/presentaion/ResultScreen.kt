package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenFiles
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.compose.sultan.plagiarismchecker.presentaion.components.FileSimilarity
import com.compose.sultan.plagiarismchecker.presentaion.components.ItemSimilarity
import java.util.ArrayList

@Composable
fun ResultScreen(navController: NavController, list: List<SimilarityBetweenString>,
                 totalSimilarityBetweenFilesList:String){
    Column() {
        LazyColumn {
            items(list) { item ->
                ItemSimilarity(item = item)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.popBackStack(Routes.Result.route)
            }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Total Result")
        }
    }
}