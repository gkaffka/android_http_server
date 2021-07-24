package com.kaffka.httpserver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CallDao {
    @Query("SELECT * FROM call ORDER BY start_date DESC")
    suspend fun getCalls(): List<Call>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalls(calls: List<Call>)
}
