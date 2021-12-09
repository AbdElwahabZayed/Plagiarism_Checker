package com.compose.sultan.plagiarismchecker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.compose.sultan.plagiarismchecker.model.MyFile
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM myfile")
    fun getAll(): Flow<List<MyFile>>

    @Query("SELECT * FROM myfile WHERE name LIKE :name AND path LIKE :path LIMIT 1")
    fun findByName(name: String, path: String): Flow<MyFile>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(vararg files: MyFile)

    @Insert(onConflict = REPLACE)
    suspend fun insertFile(file: MyFile)

    @Delete
    suspend fun delete(file: MyFile)
}
