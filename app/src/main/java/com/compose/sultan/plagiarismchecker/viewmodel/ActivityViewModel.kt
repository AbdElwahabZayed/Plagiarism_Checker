package com.compose.sultan.plagiarismchecker.viewmodel

import androidx.lifecycle.*
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

@HiltViewModel
class ActivityViewModel @Inject constructor(private val repo:Repository) : ViewModel() {
    val myFiles:LiveData<List<MyFile>> = repo.filesFlow.asLiveData()

    val mFiles: Flow<List<MyFile>> = repo.filesFlow
    suspend fun getFiles() = mFiles.first()
    fun insertFile(myFile: MyFile){
       viewModelScope.launch{
            repo.insertFile(myFile)
       }
    }
}