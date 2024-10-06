package com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sultan.plagiarismchecker.model.SimilarityWithFile
import com.compose.sultan.plagiarismchecker.model.SimilarityWithParagraph
import com.compose.sultan.plagiarismchecker.presentaion.main.readFromRawResource
import com.compose.sultan.plagiarismchecker.presentaion.main.removeWordsFromString
import com.compose.sultan.plagiarismchecker.repo.Repository
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.streams.asSequence

const val GLOBAL_ALLOWED_PERCENTAGE = 0.27

@HiltViewModel
class DataBaseCompareViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repo: Repository
) : ViewModel() {
    var firstText by mutableStateOf("")
    var firstFileParagraphs: List<String> = mutableListOf()
    var progressImportFromFirstFile by mutableStateOf(false)
    var progressImportFromFirstDb by mutableStateOf(false)
    var showDialog by mutableStateOf(false)
    var similarityWithParagraphs by mutableStateOf<List<SimilarityWithParagraph>>(mutableListOf())
    var totalSimilarityRatioBetweenFiles by mutableStateOf<List<SimilarityWithFile>>(mutableListOf())

    fun setFirstData(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firstFileParagraphs = LevenshteinDistance.readFromFile(File(path))
            withContext(Dispatchers.Main) {
                firstText = firstFileParagraphs.parallelStream().filter { it.isNotBlank() }
                    .collect(Collectors.joining("\n"))
                progressImportFromFirstFile = false
                progressImportFromFirstDb = false
            }
        }
    }


    fun compare(afterCalculate: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val list = mutableListOf<SimilarityWithFile>()
            val arr = mutableListOf<SimilarityWithParagraph>()
            val myFiles = repo.filesFlow.first()
            val removedWords = withContext(Dispatchers.IO) { context.readFromRawResource() }
            myFiles.parallelStream().forEach { file ->
                var fileSimilarityWithFile = 0.0
                val fileParagraphs =
                    LevenshteinDistance.readFromFile(File(file.path)).parallelStream()
                        .filter { it.length > 400 }
                        .asSequence().toList()
                fileParagraphs.parallelStream().forEach { outerParagraph ->
                    var maxParagraphSimilarityWithOtherParagraphs = 0.0
                    firstFileParagraphs.parallelStream().filter { it.length > 400 }
                        .forEach { innerParagraph ->
                            val text1 = removeWordsFromString(innerParagraph, removedWords)
                            val text2 = removeWordsFromString(outerParagraph, removedWords)
                            val currentRatio =
                                LevenshteinDistance.similarity(text1, text2)
                            if (currentRatio > maxParagraphSimilarityWithOtherParagraphs)
                                maxParagraphSimilarityWithOtherParagraphs = currentRatio
                            if (currentRatio > GLOBAL_ALLOWED_PERCENTAGE) {
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
                list.add(
                    SimilarityWithFile(
                        fileSimilarityWithFile,
                        file.name ?: "Anonymous"
                    )
                )
                Log.e("TAG", "compare:end ")
            }
            withContext(Dispatchers.Main) {
                similarityWithParagraphs = arr
                totalSimilarityRatioBetweenFiles = list
                Log.e("TAG", "compare: last")
                afterCalculate()
            }
        }
    }
}


