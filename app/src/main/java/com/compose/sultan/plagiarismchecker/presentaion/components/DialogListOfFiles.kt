package com.compose.sultan.plagiarismchecker.presentaion.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.compose.sultan.plagiarismchecker.model.MyFile



@Composable
fun DialogListOfFiles(
    files: List<MyFile>,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onClickItem: (Uri) -> Unit,
) {
    val dialogWidth = 250.dp
    if (showDialog) {
        Dialog(onDismissRequest = {
            setShowDialog(false)
        }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                Modifier
                    .size(dialogWidth)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp)),
            ) {
                LazyColumn {
                    items(files) { file ->
                        FileCard(file,
                            onClick = {
                                setShowDialog(false)
                                onClickItem(Uri.parse(file.path))
                            }
                        )
                    }
                }
            }
        }
    }
}
