package com.compose.sultan.plagiarismchecker.presentaion.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.presentaion.components.DefaultSnackbar
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogListOfFiles
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogResults


@Composable
fun MainScreen(
    activity: MainActivity,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val (textPosition, setTextPosition) = remember { mutableIntStateOf(1) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "First Text",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.firstText,
                onValueChange = { viewModel.firstText = it },
                label = { Text("First Text") },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.progressImportFromFirstFile) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 64.dp))
                } else {
                    Button(
                        onClick = {
                            activity.filePicker.pickFile {
                                val path = it?.file?.path ?: return@pickFile
                                viewModel.setFirstData(path)
                            }
                        }
                    ) {
                        Text(text = "From File")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (viewModel.progressImportFromFirstDb) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 64.dp))
                } else {
                    Button(onClick = {
                        setTextPosition(1)
                        viewModel.showDialog = true
                        viewModel.progressImportFromFirstDb = true
                    }) {
                        Text(text = "From DB")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Sec Text",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.secondText,
                onValueChange = { viewModel.secondText = it },
                label = { Text("Sec Text") },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.progressImportFromSecondFile) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 60.dp))
                } else {
                    Button(
                        onClick = {
                            if (activity.checkPermission()) {
                                viewModel.progressImportFromSecondFile = true
                                activity.filePicker.pickFile {
                                    val path = it?.file?.path ?: return@pickFile
                                    viewModel.setSecondData(path)
                                }
                            } else {
                                activity.requestPermission()
                                println("in Btn1 Else")
                            }
                        }
                    ) {
                        Text(text = "From File")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (viewModel.progressImportFromSecDb) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 60.dp))
                } else {
                    Button(onClick = {
                        setTextPosition(2)
                        viewModel.showDialog = true
                        viewModel.progressImportFromSecDb = true
                    }) {
                        Text(text = "From DB")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (viewModel.progressCheck) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Button(
                onClick = viewModel::compare,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "CHECK PARAGLISIM")
            }
        }
        DefaultSnackbar(
            snackbarHostState = scaffoldState.snackbarHostState,
            onDismiss = {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            },
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val files: List<MyFile> by activity.myViewModel.myFiles.collectAsStateWithLifecycle()
        DialogListOfFiles(
            files,
            viewModel.showDialog,
            { viewModel.showDialog = it },
            {
                if (textPosition == 1) {
                    it.path?.let { path ->
                        viewModel.setFirstData(path)
                    }
                } else {
                    it.path?.let { path ->
                        viewModel.setSecondData(path)
                    }
                }
            }
        )
        DialogResults(
            showDialog = viewModel.showResultDialog,
            setShowDialog = { viewModel.showResultDialog = it },
            progress = viewModel.progress
        )
    }


}


