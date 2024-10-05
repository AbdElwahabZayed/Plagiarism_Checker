package com.compose.sultan.plagiarismchecker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    val myFiles = repo.filesFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun insertFile(myFile: MyFile) {
        viewModelScope.launch {
            repo.insertFile(myFile)
        }
    }
}