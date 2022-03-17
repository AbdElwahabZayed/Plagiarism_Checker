package com.compose.sultan.plagiarismchecker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimilarityWithParagraph(val paragraph:String, val ratio:Double, val fileName:String):Parcelable
@Parcelize
data class SimilarityWithFile(val ratio:Double, val fileName:String):Parcelable
