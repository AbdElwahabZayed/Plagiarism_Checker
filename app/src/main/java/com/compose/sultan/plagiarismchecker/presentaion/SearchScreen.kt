package com.compose.sultan.plagiarismchecker.presentaion

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.onEach
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance.readWordDocFromUri
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


@Composable
fun SearchScreen(activity: MainActivity, navController: NavController, text: String) {
    val arr = remember {
        ArrayList<SimilarityBetweenString>()
    }
    val scope= rememberCoroutineScope()
    LaunchedEffect(""){
        CoroutineScope(this.coroutineContext).launch {
            Log.e("SearchScreen", "SearchScreen: *-${text}")
            Log.e("SearchScreen", "SearchScreen: *-${text.split("`~`").size}")
            activity.myViewModel.mFiles.onEach {
                it.forEach { file ->
                    text.split("`~`").forEach { str1 ->
                        readWordDocFromUri(Uri.parse(file.path), activity).forEach { str2 ->
                            val ratio = LevenshteinDistance.similarity(str1, str2)
                            if (ratio > 0.19 && str2.length > 30) {
                                arr.add(SimilarityBetweenString(str2, ratio, file.name ?: ""))
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    val gson = Gson()
                    navController.popBackStack(Routes.Search.route,true)
                    navController.navigate(
                        Routes.ResultResult.route,
                        Bundle().apply { putString("items", gson.toJson(arr)) })
                }
            }.launchIn(this)
        }
    }
 

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
    ) {

        GlideImage(
            imageModel = R.drawable.search,
            contentDescription = "Search",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

