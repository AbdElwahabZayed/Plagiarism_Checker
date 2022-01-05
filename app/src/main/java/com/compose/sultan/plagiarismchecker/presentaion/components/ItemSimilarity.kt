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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString


@Composable
fun ItemSimilarity(item: SimilarityBetweenString) {
    val rounded = item.ratio.toFloat()
    Column(Modifier.padding(all = 8.dp)) {
        Text(text = item.pragraph, style = TextStyle(color = Color.Black, fontSize = 15.sp))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.fileName.replace("primary", "File Name"),
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(2f),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
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
                    color = MaterialTheme.colors.primary,
                    progress = rounded,
                    strokeWidth = 1.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent, shape = CircleShape)
                )
                Text(text = "${(rounded * 100).toInt()}%", color = MaterialTheme.colors.primary)
            }
        }
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
    }
}