package com.compose.sultan.plagiarismchecker.presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import java.math.RoundingMode

@Composable
fun ItemSimilarity(item:SimilarityBetweenString){
    val rounded = item.ratio.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
    Column(Modifier.padding(all = 8.dp)) {
        Text(text = item.pragraph)
        Spacer(modifier = Modifier.width(8.dp))
        Row {
            Text(text = item.fileName)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                Modifier
                    .size(25.dp)
                    .background(MaterialTheme.colors.background, shape = CircleShape)
                    .padding(8.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    progress = item.ratio.toFloat(),
                    strokeWidth = 5.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent, shape = CircleShape)
                )
                Column(modifier =  Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${rounded * 100}%",color = MaterialTheme.colors.primary)
                }
            }
        }
    }
}