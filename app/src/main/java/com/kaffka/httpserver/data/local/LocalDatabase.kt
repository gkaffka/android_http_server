package com.kaffka.httpserver.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Call::class, CallStatus::class, ServerLog::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun callDao(): CallDao
    abstract fun callStatusDao(): CallStatusDao
    abstract fun serverLogDao(): ServerLogDao
}
