package com.example.appnasapi.bd

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PODDao {

    @Query("SELECT * FROM Pods")
    fun getAllPods(): Flow<MutableList<POD>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg pod: POD)

    @Query("DELETE FROM Pods WHERE date = :date")
    suspend fun delete(date: String)
}