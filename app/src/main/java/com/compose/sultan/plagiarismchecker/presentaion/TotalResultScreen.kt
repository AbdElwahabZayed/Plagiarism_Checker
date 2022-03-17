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
import com.compose.sultan.plagiarismchecker.model.SimilarityWithFile
import com.compose.sultan.plagiarismchecker.presentaion.components.FileSimilarity


@Composable
fun TotalResultScreen(navController: NavController, list: List<SimilarityWithFile>) {
    Column() {
        LazyColumn {
            items(list) { item ->
                FileSimilarity(item = item)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.popBackStack()
            }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "OK")
        }
    }
}