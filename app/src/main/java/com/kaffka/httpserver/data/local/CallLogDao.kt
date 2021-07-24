package com.kaffka.httpserver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServerLogDao {
    @Query("SELECT * FROM ServerLog ")
    suspend fun getServerLog(): List<ServerLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServerLog(serverLog: ServerLog)
}
