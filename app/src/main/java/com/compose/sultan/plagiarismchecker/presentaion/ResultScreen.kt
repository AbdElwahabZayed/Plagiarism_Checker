package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.compose.sultan.plagiarismchecker.presentaion.components.ItemSimilarity
import java.util.ArrayList

@Composable
fun ResultScreen(navController: NavController, list: List<SimilarityBetweenString>){
    LazyColumn{
        items(list){ item ->
            ItemSimilarity(item = item)
        }
    }
}