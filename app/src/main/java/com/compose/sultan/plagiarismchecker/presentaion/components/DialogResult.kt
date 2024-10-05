package com.compose.sultan.plagiarismchecker.presentaion.components

import android.util.Log
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private const val TAG = "DialogResult"
@Composable
fun DialogResults(showDialog: Boolean, setShowDialog: (Boolean) -> Unit, progress: Float) {
    val dialogWidth = 250.dp
    Log.e(
        TAG,
        "DialogResults() called with: showDialog = $showDialog, setShowDialog = $setShowDialog, progress = $progress"
    )
    if (showDialog) {
        Dialog(onDismissRequest = { setShowDialog(false) }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                Modifier
                    .size(dialogWidth)
                    .background(MaterialTheme.colors.background, shape = CircleShape)
                    .padding(8.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    progress = progress,
                    strokeWidth = 5.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent, shape = CircleShape)
                )

                Column(modifier =  Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Similarity ratio ${progress * 100}%",color = MaterialTheme.colors.primary)
                }
            }
        }
    }
}