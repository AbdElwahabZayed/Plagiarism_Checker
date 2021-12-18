package com.compose.sultan.plagiarismchecker.presentaion

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.Text
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance.readWordDocFromUri
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(activity: MainActivity, navController: NavController, text: Text) {
    val (ratio, setRatio) = remember { mutableStateOf(0.0) }
    val items by activity.myViewModel.myFiles.observeAsState(null)
    if(items!=null)
    rememberCoroutineScope {
        CoroutineScope(Dispatchers.Default).launch {
            Log.e("SearchScreen", "SearchScreen: ${ text.text.split("\n").size}")
            Log.e("SearchScreen", "SearchScreen: ${ text.text}")
            if (true) {
//                it.forEach {
//                    val uri = Uri.parse(it.path)
//                    val docString: String = DocumentReaderUtil.readWordDocFromUri(
//                        uri,
//                        activity.applicationContext
//                    )
//                    val dcString: String = DocumentReaderUtil.readWordDocFromUri(
//                        uri,
//                        activity.applicationContext
//                    )
//                    Log.e("SearchScreen", "SearchScreen: $uri")
//                    Log.e("SearchScreen", "SearchScreen: count = > ${text.text.split(Regex("^\\t*\$")).size}")
//                    Log.e("SearchScreen", "SearchScreen: count = > ${docString.split(Regex("^\\t*\$")).size}")
//                    text.text.split(Regex("^\\t*\$")).forEach { str1 ->
//                        println(str1)
//                        docString.split(Regex("^\\t*\$")).forEach { str2 ->
//                            println(str2)
//                            setRatio(LevenshteinDistance.similarity(str1, str2))
//                            println(ratio)
//                        }
//                    }
//
//
//                    val rounded = ratio.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
//                    println("the res it == $rounded")
//                }

            }

            items?.forEach { file ->
                text.text.split("\n").forEach {  str1->
                    readWordDocFromUri(Uri.parse(file.path),activity).forEach {  str2->
                        Log.e("SearchScreen", "SearchScreen: ${str1}")
                        Log.e("SearchScreen", "SearchScreen: ${str2}")
                        setRatio(LevenshteinDistance.similarity(str1,str2))
                        Log.e("SearchScreen", "SearchScreen: ${ratio}")
                    }
                }
            }
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
        ) {

        }


    }
}

