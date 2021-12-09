package com.compose.sultan.plagiarismchecker.presentaion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.MyFile

@Composable
fun FileCard(
    file: MyFile,
//    activity: MainActivity,
//    textPosition: MutableState<Int>,
//    text1: String,
//    text2: String,
//    showDialog: Boolean,
//    setShowDialog: (Boolean) -> Unit,
    onClick: () -> Unit
){

    Column {
        Spacer(modifier = Modifier.height(4.dp))
        Surface(shape= MaterialTheme.shapes.medium,
            elevation = 1.dp,
            modifier = Modifier.padding(1.dp).fillMaxWidth(),
            color = MaterialTheme.colors.surface ) {
            Text(text = file.name?.split("/")?.last()?:"",
                style= MaterialTheme.typography.body2,
                modifier = Modifier.padding(all = 4.dp).clickable(onClick = onClick))
        }

    }
}