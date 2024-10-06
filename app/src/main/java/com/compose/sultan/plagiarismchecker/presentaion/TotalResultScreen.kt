package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.presentaion.components.FileSimilarity
import com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen.DataBaseCompareViewModel

@Composable
fun TotalResultScreen(
    navController: NavController,
    viewModel: DataBaseCompareViewModel
) {
    val totalRatio =
        viewModel.totalSimilarityRatioBetweenFiles.sumOf { it.ratio } / viewModel.totalSimilarityRatioBetweenFiles.size
    val refactoring = if (totalRatio > 1.0) 1.0 else totalRatio
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Total Index Similarity",
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(2f),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                Modifier
                    .size(65.dp)
                    .background(MaterialTheme.colors.background, shape = CircleShape)
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    progress = refactoring.toFloat(),
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent, shape = CircleShape)
                )
                Text(text = "${(refactoring * 100).toInt()}%", color = Color.Black)
            }

        }
        Box(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        LazyColumn {
            items(viewModel.totalSimilarityRatioBetweenFiles) { item ->
                FileSimilarity(item = item)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.navigate(Routes.Menu.route) {
                    popUpTo(Routes.Menu.route) { inclusive = true }
                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "OK")
        }
    }
}