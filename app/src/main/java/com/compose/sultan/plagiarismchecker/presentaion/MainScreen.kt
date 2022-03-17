package com.compose.sultan.plagiarismchecker.presentaion

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.presentaion.components.DefaultSnackbar
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogListOfFiles
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogResults
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance
import com.thoughtleaf.textsumarizex.DocumentReaderUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode


@Composable
fun MainScreen(activity: MainActivity) {
    // State to manage if the alert dialog is showing or not.
    // Default is false (not showing)
    val (showResultDialog, setShowResultDialog) = remember { mutableStateOf(false) }
    val (progress, setProgress) = remember { mutableStateOf(0F) }
        val (text1, setText1) = remember { mutableStateOf("") }
    val (text2, setText2) = remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (progressImportFromFile1, setProgressImportFromFile1) = remember { mutableStateOf(false) }
    val (progressImportFromFile2, setProgressImportFromFile2) = remember { mutableStateOf(false) }
    val (progressImportFromDB1, setProgressImportFromDB1) = remember { mutableStateOf(false) }
    val (progressImportFromDB2, setProgressImportFromDB2) = remember { mutableStateOf(false) }
    val (progressCheck, setProgressCheck) = remember { mutableStateOf(false) }
    val (textPosition, setTextPosition) = remember { mutableStateOf(1) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                activity.lifecycleScope.launch {
                    it.data?.let { myData ->
                        val docString: String = DocumentReaderUtil.readWordDocFromUri(
                            myData.data,
                            activity.applicationContext
                        )
                        withContext(Dispatchers.Main) {
                            setText1(docString)
                            setProgressImportFromFile1(false)
                        }
                    }
                }
            } else {
                setProgressImportFromFile1(false)
            }
        }
    val launcher1 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                activity.lifecycleScope.launch {
                    it.data?.let { myData ->
                        val docString: String = DocumentReaderUtil.readWordDocFromUri(
                            myData.data,
                            activity.applicationContext
                        )
                        withContext(Dispatchers.Main) {
                            setText2(docString)
                            setProgressImportFromFile2(false)
                        }
                    }
                }
            } else {
                setProgressImportFromFile2(false)
            }
        }
    val fabLuncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { myData ->
                val myFile = MyFile(
                    myData.data?.pathSegments?.last(),
                    myData.data?.toString() ?: ""
                )
                activity.myViewModel.insertFile(myFile = myFile)
                Toast.makeText(activity, "File Added Successfully", Toast.LENGTH_LONG).show()
                println("File Added Successfully")
            }
        }
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
                value = text1,
                onValueChange = { setText1(it) },
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
                if (progressImportFromFile1) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 64.dp))
                } else {
                    Button(onClick = {
                        if (activity.checkPermission()) {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            launcher.launch(intent)
                            setProgressImportFromFile1(true)
                        } else {
                            activity.requestPermission()
                            println("in Btn1 Else")
                        }
                    }) {
                        Text(text = "From File")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (progressImportFromDB1) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 64.dp))
                } else {
                    Button(onClick = {
                        setTextPosition(1)
                        setShowDialog(true)
                        setProgressImportFromDB1(true)
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
                value = text2,
                onValueChange = { setText2(it) },
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
                if (progressImportFromFile2) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 60.dp))
                } else {
                    Button(onClick = {
                        if (activity.checkPermission()) {
                            setProgressImportFromFile2(true)
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.type = "*/*"
                            launcher1.launch(intent)
                        } else {
                            activity.requestPermission()
                            println("in Btn1 Else")
                        }
                    }) {
                        Text(text = "From File")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (progressImportFromDB2) {
                    CircularProgressIndicator(modifier = Modifier.padding(horizontal = 60.dp))
                } else {
                    Button(onClick = {
                        setTextPosition(2)
                        setShowDialog(true)
                        setProgressImportFromDB2(true)
                    }) {
                        Text(text = "From DB")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (progressCheck) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Button(
                onClick = {
                    setProgressCheck(true)
                    CoroutineScope(Dispatchers.Default).launch {
                        val ratio = LevenshteinDistance.similarity(text1, text2)
                        val rounded = ratio.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
                        println("the res it == $rounded")
                        withContext(Dispatchers.Main) {
                            setProgress(ratio.toFloat())
                            setShowResultDialog(true)
                            setProgressCheck(false)
                        }
                    }
                },
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
//        FloatingActionButton(
//            onClick = {
//                if (activity.checkPermission()) {
//                    val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    intent.type = "*/*"
//                    fabLuncher.launch(intent)
//                } else {
//                    activity.requestPermission()
//                    println("in Btn1 Else")
//                }
//            }, modifier = Modifier
//                .align(Alignment.End)
//                .padding(all = 4.dp), elevation = FloatingActionButtonDefaults.elevation(2.dp, 4.dp)
//        ) {
//            Icon(Icons.Filled.Add, "", tint = Color.White)
//        }
        val files: List<MyFile> by activity.myViewModel.myFiles.observeAsState(listOf())
        DialogListOfFiles(
            files,
            textPosition,
            setText1,
            setText2,
            showDialog,
            setShowDialog,
            setProgressImportFromDB1,
            setProgressImportFromDB2
        )
        DialogResults(
            showDialog = showResultDialog,
            setShowDialog = setShowResultDialog,
            progress = progress

        )
    }


}


