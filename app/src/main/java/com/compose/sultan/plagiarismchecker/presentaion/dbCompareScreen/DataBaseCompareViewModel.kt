package com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sultan.plagiarismchecker.model.SimilarityWithFile
import com.compose.sultan.plagiarismchecker.model.SimilarityWithParagraph
import com.compose.sultan.plagiarismchecker.model.getParagraphs
import com.compose.sultan.plagiarismchecker.presentaion.main.readFromRawResource
import com.compose.sultan.plagiarismchecker.presentaion.main.removeWordsFromString
import com.compose.sultan.plagiarismchecker.repo.Repository
import com.compose.sultan.plagiarismchecker.service.ArabicStemmer
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.streams.asSequence

@HiltViewModel
class DataBaseCompareViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repo: Repository
) : ViewModel() {
    private val stemmer = ArabicStemmer()
    var firstText by mutableStateOf("")
    var progressImportFromFirstFile by mutableStateOf(false)
    var progressImportFromFirstDb by mutableStateOf(false)
    var showDialog by mutableStateOf(false)
    lateinit var arr: MutableList<SimilarityWithParagraph>
    lateinit var totalSimilarityRatioBetweenFiles: MutableList<SimilarityWithFile>


    fun setFirstData(uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val docString =
                LevenshteinDistance.readWordDocFromUri(uri, context)
            val removedWords = withContext(Dispatchers.IO) { context.readFromRawResource() }
            val text = removeWordsFromString(docString.toString(), removedWords)
            withContext(Dispatchers.Main) {
                firstText = text
                progressImportFromFirstFile = false
            }
        }
    }


    fun compare(afterCalculate: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val myFiles = repo.filesFlow.first()
            Log.e("SearchScreen2", "SearchScreen: *-${firstText}")
            Log.e("SearchScreen2", "SearchScreen: *-${firstText.split("`~`").size}")
            myFiles.parallelStream().forEach { file ->
                var fileSimilarityWithFile = 0.0
                val fileParagraphs =
                    file.getParagraphs(context).parallelStream().filter { it.length > 30 }
                        .asSequence().toList()
                stemmer.process(fileParagraphs).parallelStream().forEach { outerParagraph ->
                    var maxParagraphSimilarityWithOtherParagraphs = 0.0
                    firstText.split("`~`").parallelStream().filter { it.length > 30 }
                        .forEach { innerParagraph ->
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
                afterCalculate()
            }
        }
    }
}


