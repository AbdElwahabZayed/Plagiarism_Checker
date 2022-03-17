package com.compose.sultan.plagiarismchecker.model

import android.content.Context
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.sultan.plagiarismchecker.service.LevenshteinDistance.readWordDocFromUri

@Entity
data class MyFile(
    @ColumnInfo(name = "name") val name: String?,
    @PrimaryKey @ColumnInfo(name = "path") val path: String,
)

fun MyFile.getParagraphs(context: Context?) = readWordDocFromUri(Uri.parse(this.path), context)