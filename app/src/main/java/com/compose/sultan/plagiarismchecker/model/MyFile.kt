package com.compose.sultan.plagiarismchecker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyFile(
    @ColumnInfo(name = "name") val name: String?,
    @PrimaryKey @ColumnInfo(name = "path") val path: String,
)
