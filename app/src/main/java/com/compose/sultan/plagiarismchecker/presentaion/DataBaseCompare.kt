package com.compose.sultan.plagiarismchecker.presentaion

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogListOfFiles
import com.thoughtleaf.textsumarizex.DocumentReaderUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DataBaseCompare(activity: MainActivity){
    val (text1, setText1) = remember { mutableStateOf("") }
    val (progressImportFromFile1, setProgressImportFromFile1) = remember { mutableStateOf(false) }
    val (progressImportFromDB1, setProgressImportFromDB1) = remember { mutableStateOf(false) }
    val (textPosition, setTextPosition) = remember { mutableStateOf(1) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

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
    }
    val files: List<MyFile> by activity.myViewModel.myFiles.observeAsState(listOf())
    DialogListOfFiles(
        files,
        textPosition,
        setText1,
        {  },
        showDialog,
        setShowDialog,
        setProgressImportFromDB1,
        {  }
    )
}