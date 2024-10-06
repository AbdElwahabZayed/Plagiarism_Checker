package com.compose.sultan.plagiarismchecker.presentaion

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compose.sultan.plagiarismchecker.MainActivity
import com.compose.sultan.plagiarismchecker.R
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.compose.sultan.plagiarismchecker.ui.theme.PlagiarismCheckerTheme

@Composable
fun MenuScreen(activity: MainActivity, navController: NavController) {
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
                activity.filePicker.pickFile {
                    val name = it?.name ?: return@pickFile
                    val path = it.file?.path ?: return@pickFile
                    val myFile = MyFile(
                        name,
                        path
                    )
                    activity.myViewModel.insertFile(myFile)
                    Toast.makeText(activity, "File Added Successfully", Toast.LENGTH_LONG).show()
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
    val navController: NavHostController = rememberNavController()
    PlagiarismCheckerTheme {
        MenuScreen(MainActivity(), navController = navController)
    }
}

