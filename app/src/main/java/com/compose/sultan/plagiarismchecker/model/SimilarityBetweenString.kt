package com.compose.sultan.plagiarismchecker.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SimilarityBetweenString(@Json(name = "pragraph") val pragraph:String,@Json(name = "ratio") val ratio:Double,@Json(name = "fileName")  val fileName:String):Parcelable
