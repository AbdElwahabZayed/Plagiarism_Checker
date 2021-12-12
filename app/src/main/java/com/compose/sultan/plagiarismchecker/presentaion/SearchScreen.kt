package com.compose.sultan.plagiarismchecker.presentaion

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SearchScreen(activity: MainActivity,navController: NavController){
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center,
    ) {
        GlideImage(imageModel = R.drawable.search, contentDescription = "Search",modifier = Modifier.fillMaxWidth(),contentScale = ContentScale.FillWidth )
    }
}