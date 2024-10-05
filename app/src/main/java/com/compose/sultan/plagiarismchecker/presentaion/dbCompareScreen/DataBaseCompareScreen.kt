package com.compose.sultan.plagiarismchecker.presentaion.dbCompareScreen

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.presentaion.Routes
import com.compose.sultan.plagiarismchecker.presentaion.components.DialogListOfFiles

@Composable
fun DataBaseCompareScreen(
    activity: MainActivity,
    navController: NavController,
    viewModel: DataBaseCompareViewModel = hiltViewModel()
) {
    val (textPosition, setTextPosition) = remember { mutableStateOf(1) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                viewModel.setFirstData(data.data)
            } ?: run { viewModel.progressImportFromFirstFile = false }
        }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {

        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Text",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.firstText,
                onValueChange = { /*setText1(it)*/ },
                label = { Text("Text") },
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
                    Button(onClick = {
                        if (activity.checkPermission()) {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "*/*"
                            launcher.launch(intent)
                            viewModel.progressImportFromFirstFile = true
                        } else {
                            activity.requestPermission()
                            println("in Btn1 Else")
                        }
                    }) {
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
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                Log.e("SearchScreen", "SearchScreen: **- ${viewModel.firstText}")
                navController.navigate(Routes.Search.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Compare with DB")
        }
        Spacer(modifier = Modifier.width(8.dp))
        val files: List<MyFile> by activity.myViewModel.myFiles.collectAsStateWithLifecycle()
        DialogListOfFiles(
            files,
            viewModel.showDialog,
            { viewModel.showDialog = it },
            {
                viewModel.setFirstData(it)
            }
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DBScreen() {
//    PlagiarismCheckerTheme {
//        DataBaseCompareScreen(MainActivity())
//    }
//}