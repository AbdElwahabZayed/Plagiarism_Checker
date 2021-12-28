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
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.SimilarityBetweenString
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance.readWordDocFromUri
import com.skydoves.landscapist.glide.GlideImage
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
    rememberCoroutineScope {
        CoroutineScope(Dispatchers.Default).launch {
            Log.e("SearchScreen", "SearchScreen: *-${text}")
            Log.e("SearchScreen", "SearchScreen: *-${text.split("`~`").size}")
            activity.myViewModel.mFiles.onEach {
                it.forEach { file ->
                    text.split("`~`").forEach { str1 ->
                        readWordDocFromUri(Uri.parse(file.path), activity).forEach { str2 ->
                            val ratio = LevenshteinDistance.similarity(str1, str2)
                            if (ratio > 0.19) {
                                arr.add(SimilarityBetweenString(str1, ratio, file.name ?: ""))
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    val moshi = Moshi.Builder().build()
                    val type = Types.newParameterizedType(List::class.java, SimilarityBetweenString::class.java)
                    val adapter = moshi.adapter<List<SimilarityBetweenString>>(type)
                    navController.navigate(Routes.ResultResult.route,
                        Bundle().apply { putString("items", adapter.toJson(arr.toList())) })
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

