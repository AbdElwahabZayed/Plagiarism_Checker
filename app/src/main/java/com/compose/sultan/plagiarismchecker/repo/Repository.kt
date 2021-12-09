package com.compose.sultan.plagiarismchecker.repo

import androidx.room.Room
import com.compose.sultan.plagiarismchecker.MyApplication
import com.compose.sultan.plagiarismchecker.db.AppDatabase
import com.compose.sultan.plagiarismchecker.db.FileDao
import com.compose.sultan.plagiarismchecker.model.MyFile
import kotlinx.coroutines.flow.Flow

object Repository {
    private val db:AppDatabase = Room.databaseBuilder(
        MyApplication.getAppInstance(),
        AppDatabase::class.java,
        "MyFile").build()
    private val fileDao:FileDao = db.fileDao()


    fun getInstance():Repository = this

    val filesFlow:Flow<List<MyFile>>
        get() = fileDao.getAll()


    fun findByName(name: String, path: String): Flow<MyFile> = fileDao.findByName(name ,path)

    suspend fun insertAll(vararg files: MyFile) = fileDao.insertAll(*files)
    suspend fun insertFile(file: MyFile) {
        fileDao.insertFile(file)
    }

    suspend fun delete(user: MyFile) = fileDao.delete(user)

}