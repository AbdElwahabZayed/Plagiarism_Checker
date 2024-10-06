package com.compose.sultan.plagiarismchecker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.atwa.filepicker.core.FilePicker
import com.compose.sultan.plagiarismchecker.presentaion.Navigation
import com.compose.sultan.plagiarismchecker.ui.theme.PlagiarismCheckerTheme
import com.compose.sultan.plagiarismchecker.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val myViewModel: ActivityViewModel by viewModels()
    lateinit var filePicker: FilePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePicker = FilePicker.getInstance(this)
        setContent {
            PlagiarismCheckerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(this)
                }
            }
        }
    }

}

