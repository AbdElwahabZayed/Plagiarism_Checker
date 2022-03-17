package com.compose.sultan.plagiarismchecker.presentaion

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.SimilarityWithFile
import com.compose.sultan.plagiarismchecker.model.SimilarityWithParagraph
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import com.compose.sultan.plagiarismchecker.utils.Constants.ITEMS
import com.compose.sultan.plagiarismchecker.utils.Constants.TOTAL_SIMILARITY
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.getParagraphs

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


@Composable
fun SearchScreen(activity: MainActivity, navController: NavController, text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        GlideImage(
            imageModel = R.drawable.search,
            contentDescription = "Search",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
    val arr = remember {
        ArrayList<SimilarityWithParagraph>()
    }
    val totalSimilarityRatioBetweenFiles = remember {
        ArrayList<SimilarityWithFile>()
    }
    //val scope = rememberCoroutineScope()

    LaunchedEffect("") {
        CoroutineScope(this.coroutineContext).launch {
            val myFiles = activity.myViewModel.getFiles()
            Log.e("SearchScreen2", "SearchScreen: *-${text}")
            Log.e("SearchScreen2", "SearchScreen: *-${text.split("`~`").size}")
            myFiles.forEach { file ->
                var fileSimilarityWithFile = 0.0
                val fileParagraphs = file.getParagraphs(activity).filter { it.length > 30 }
                fileParagraphs.forEach { outerParagraph ->
                    var maxParagraphSimilarityWithOtherParagraphs = 0.0
                    text.split("`~`").filter { it.length > 30 }.forEach { innerParagraph ->
                        val currentRatio =
                            LevenshteinDistance.similarity(outerParagraph, innerParagraph)
                        if (currentRatio > maxParagraphSimilarityWithOtherParagraphs)
                            maxParagraphSimilarityWithOtherParagraphs = currentRatio
                        if (currentRatio > 0.19) {
                            arr.add(
                                SimilarityWithParagraph(
                                    outerParagraph,
                                    currentRatio,
                                    file.name ?: "Anonymous"
                                )
                            )
                        }
                    }
                    fileSimilarityWithFile += maxParagraphSimilarityWithOtherParagraphs
                }
                fileSimilarityWithFile /= fileParagraphs.size
                totalSimilarityRatioBetweenFiles.add(
                    SimilarityWithFile(
                        fileSimilarityWithFile,
                        file.name ?: "Anonymous"
                    )
                )
            }
            withContext(Dispatchers.Main) {
                val gson = Gson()
                navController.popBackStack(Routes.Search.route, true)
                navController.navigate(
                    Routes.Result.route,
                    Bundle().apply {
                        putString(ITEMS, gson.toJson(arr))
                        putString(
                            TOTAL_SIMILARITY,
                            gson.toJson(totalSimilarityRatioBetweenFiles)
                        )
                    })
            }
        }
    }



}


