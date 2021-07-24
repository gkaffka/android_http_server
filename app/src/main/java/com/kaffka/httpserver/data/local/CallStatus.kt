package com.kaffka.httpserver.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CallStatus(
    @PrimaryKey @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "state") val state: Int
)
