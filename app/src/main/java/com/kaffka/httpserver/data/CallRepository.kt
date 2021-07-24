package com.kaffka.httpserver.data

import android.content.ContentResolver
import android.provider.CallLog.Calls.*
import com.kaffka.httpserver.data.local.CallDao
import com.kaffka.httpserver.domain.Call
import toDomain
import javax.inject.Inject
import com.kaffka.httpserver.data.local.Call as CallDB


class CallRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val callDao: CallDao
) {

    suspend fun getFreshCallLogs(): List<Call> {
        callDao.insertCalls(contentResolver.getCallData())
        return callDao.getCalls().map { it.toDomain() }
    }

    private fun ContentResolver.getCallData(): List<CallDB> {
        val cursor = query(
            CONTENT_URI,
            arrayOf(DATE, DURATION, NUMBER, CACHED_NAME, TYPE),
            null,
            null,
            DATE
        ) ?: return emptyList()

        val numberIndex = cursor.getColumnIndex(NUMBER)
        val durationIndex = cursor.getColumnIndex(DURATION)
        val dateIndex = cursor.getColumnIndex(DATE)
        val nameIndex = cursor.getColumnIndex(CACHED_NAME)
        val typeIndex = cursor.getColumnIndex(TYPE)

        val calls = mutableListOf<com.kaffka.httpserver.data.local.Call>()
        while (cursor.moveToNext()) {
            val number = cursor.getString(numberIndex)
            val duration = cursor.getString(durationIndex).toLong()
            val date = cursor.getString(dateIndex).toLong()
            val name = cursor.getString(nameIndex)
            val type = cursor.getString(typeIndex).toInt()

            calls.add(
                com.kaffka.httpserver.data.local.Call(
                    startDate = date,
                    name = name,
                    type = type,
                    duration = duration,
                    number = number
                )
            )
        }
        cursor.close()
        return calls
    }
}
