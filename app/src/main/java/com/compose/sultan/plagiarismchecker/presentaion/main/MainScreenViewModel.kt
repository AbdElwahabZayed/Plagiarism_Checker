package com.compose.sultan.plagiarismchecker.presentaion.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.RoundingMode
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    var firstText by mutableStateOf("")
    var secondText by mutableStateOf("")
    var progressImportFromFirstFile by mutableStateOf(false)
    var progressImportFromSecondFile by mutableStateOf(false)
    var progressImportFromFirstDb by mutableStateOf(false)
    var progressImportFromSecDb by mutableStateOf(false)
    var progress by mutableStateOf(0F)
    var showDialog by mutableStateOf(false)
    var progressCheck by mutableStateOf(false)
    var showResultDialog by mutableStateOf(false)
    fun setFirstData(uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val docString = LevenshteinDistance.readWordDocFromUri(
                uri, context
            )
            val removedWords = withContext(Dispatchers.IO) { context.readFromRawResource() }
            val text = removeWordsFromString(docString.toString(), removedWords)
            Log.e("TAG", "setFirstData: $text")
            withContext(Dispatchers.Main) {
                firstText = text
                progressImportFromFirstFile = false
                progressImportFromFirstDb = false
            }
        }
    }

    fun setSecondData(data: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val docString = LevenshteinDistance.readWordDocFromUri(
                data, context
            )
            val removedWords = withContext(Dispatchers.IO) { context.readFromRawResource() }
            val text = removeWordsFromString(docString.toString(), removedWords)
            withContext(Dispatchers.Main) {
                secondText = text
                progressImportFromSecondFile = false
                progressImportFromSecDb = false
            }
        }
    }

    fun compare() {
        viewModelScope.launch(Dispatchers.Default) {
            val removedWords = withContext(Dispatchers.IO) { context.readFromRawResource() }
            val text1 = removeWordsFromString(firstText, removedWords)
            val text2 = removeWordsFromString(secondText, removedWords)
            Log.e("TAG", "compare: f $firstText")
            Log.e("TAG", "compare: S $secondText")
            Log.e("TAG", "compare: f $text1")
            Log.e("TAG", "compare: S $text2")
            val ratio = LevenshteinDistance.similarity(text1, text2)
            val rounded = ratio.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
            println("the res it == $rounded")
            withContext(Dispatchers.Main) {
                progress = ratio.toFloat()
                showResultDialog = true
                progressCheck = false
            }
        }
    }

    fun saveFirstFileToInternalCache(data: Uri?) {
        try {
            viewModelScope.launch {
                // Get the internal cache directory
                val cacheDir = context.cacheDir
                // Create a new file in the cache directory
                val destFile = File(cacheDir, data?.lastPathSegment ?: "FILE_${Math.random()}")

                // Open input stream from the source URI
                val inputStream: InputStream? = context.contentResolver.openInputStream(data!!)
                // Open output stream to the destination file
                val outputStream = FileOutputStream(destFile)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        // Copy the data
                        input.copyTo(output)
                    }
                }
                setFirstData(Uri.parse(destFile.absolutePath))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveSecFileToInternalCache(data: Uri?) {
        try {
            viewModelScope.launch {
                // Get the internal cache directory
                val cacheDir = context.cacheDir
                // Create a new file in the cache directory
                val destFile = File(cacheDir, data?.lastPathSegment ?: "FILE_${Math.random()}")

                // Open input stream from the source URI
                val inputStream: InputStream? = context.contentResolver.openInputStream(data!!)
                // Open output stream to the destination file
                val outputStream = FileOutputStream(destFile)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        // Copy the data
                        input.copyTo(output)
                    }
                }
                setSecondData(Uri.parse(destFile.absolutePath))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

fun Context.readFromRawResource(): List<String> {
    val stringList = mutableListOf<String>()
    val inputStream =
        resources.openRawResource(R.raw.list) // R.raw.list corresponds to list.txt

    inputStream.bufferedReader().use { reader ->
        reader.forEachLine { line ->
            stringList.add(line)
        }
    }

    return stringList
}

fun removeWordsFromString(original: String, wordsToRemove: List<String>): String {
    // Convert the list of words to a Set for faster lookup
    val wordsSet = wordsToRemove.toSet()

    // Split the original string into words and process in parallel
    return original.split(" ")
        .parallelStream() // Use parallel stream
        .filter { word -> word !in wordsSet } // Filter out the unwanted words
        .collect(Collectors.joining(" ")) // Join the remaining words with a space
}