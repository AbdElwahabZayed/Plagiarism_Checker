package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.compose.sultan.plagiarismchecker.MainActivity
import com.google.android.material.imageview.ShapeableImageView

@Composable
fun MenuScreen(activity: MainActivity) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Text(
            text = "Plagiarism Checker",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column {
//            Image(painter = , contentDescription = )
        }


    }
}
