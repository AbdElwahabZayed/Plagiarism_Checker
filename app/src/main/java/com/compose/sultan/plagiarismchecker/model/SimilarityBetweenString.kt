package com.compose.sultan.plagiarismchecker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimilarityBetweenString(val paragraph:String, val ratio:Double, val fileName:String):Parcelable
@Parcelize
data class SimilarityBetweenFiles(val ratio:Double, val fileName:String):Parcelable
