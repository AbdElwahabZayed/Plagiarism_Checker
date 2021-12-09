package com.compose.sultan.plagiarismchecker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.sultan.plagiarismchecker.model.MyFile

@Database(entities = arrayOf(MyFile::class), version = 1 ,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
}
