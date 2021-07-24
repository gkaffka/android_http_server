package com.kaffka.httpserver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CallStatusDao {
    @Query("SELECT * FROM CallStatus")
    suspend fun getCallStatus(): CallStatus?

    @Query("DELETE FROM CallStatus")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallStatus(callStatus: CallStatus)
}
