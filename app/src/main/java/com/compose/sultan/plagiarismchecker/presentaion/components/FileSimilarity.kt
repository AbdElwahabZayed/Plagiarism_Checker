package com.compose.sultan.plagiarismchecker.presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.sultan.plagiarismchecker.model.SimilarityWithFile
import java.util.Random

@Preview(showBackground = true)
@Composable
fun FileSimilarity(item: SimilarityWithFile = SimilarityWithFile(0.52, "MyFile")) {
    val rounded = item.ratio.toFloat()
    Column(Modifier.padding(all = 8.dp)) {
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
            val rnd = Random()
            val rnd2 = Random()
            val color = android.graphics.Color.argb(
                255,
                rnd.nextInt(256),
                rnd2.nextInt(256),
                rnd.nextInt(256)
            )
            Box(
                Modifier
                    .size(65.dp)
                    .background(MaterialTheme.colors.background, shape = CircleShape)
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(color),
                    progress = rounded,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent, shape = CircleShape)
                )
                Text(text = "${(rounded * 100).toInt()}%", color = Color(color))
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
