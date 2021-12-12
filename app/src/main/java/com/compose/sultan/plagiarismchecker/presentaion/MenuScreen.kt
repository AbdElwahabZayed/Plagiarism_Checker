package com.compose.sultan.plagiarismchecker.presentaion

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.ui.theme.PlagiarismCheckerTheme

@Composable
fun MenuScreen(activity: MainActivity, navController: NavController) {
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
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Plagiarism Checker",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier.clickable {
                navController.navigate("main_screen")
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_text_vs_text_mode),
                contentDescription = stringResource(
                    id = R.string.text_vs_text
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.text_vs_text),
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.clickable {
                navController.navigate("db_compare")
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_text_with_db),
                contentDescription = stringResource(
                    id = R.string.text_with_db
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.text_with_db),
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.clickable {
                if (activity.checkPermission()) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    fabLuncher.launch(intent)
                } else {
                    activity.requestPermission()
                    println("in Btn1 Else")
                }
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_file),
                contentDescription = stringResource(
                    id = R.string.add_file
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.add_file),
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    PlagiarismCheckerTheme {
        MenuScreen(MainActivity(), navController = navController)
    }
}

