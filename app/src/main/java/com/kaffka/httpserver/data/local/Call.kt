package com.kaffka.httpserver.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Call(
    @PrimaryKey @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: Int
)

