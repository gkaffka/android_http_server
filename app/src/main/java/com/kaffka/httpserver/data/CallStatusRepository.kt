package com.kaffka.httpserver.data

import com.kaffka.httpserver.data.local.CallStatusDao
import com.kaffka.httpserver.domain.CallState.*
import com.kaffka.httpserver.domain.CallStatus
import javax.inject.Inject
import com.kaffka.httpserver.data.local.CallStatus as CallStatusDb

private const val NO_ONGOING_CALL = "NO ONGOING CALL"

class CallStatusRepository @Inject constructor(private val callStatusDao: CallStatusDao) {

    suspend fun updateStatus(number: String, state: Int) {
        val currentState = state.toCallState()
        val lastState = getCallStatus().state
        if (currentState != lastState) {
            // If number is empty, try to get it from the database (This is a possibility
            val validNumber = if(number.isEmpty()) getCallStatus().number else number
            // Call ended (state IDLE) clear database
            if (currentState == IDLE) callStatusDao.deleteAll()
            // Update DB with current state (ONGOING or RINGING)
            else callStatusDao.insertCallStatus(
                CallStatusDb(
                    number = validNumber,
                    state = state
                )
            )
        }
    }

    suspend fun getCallStatus() = callStatusDao.getCallStatus()?.toDomain() ?: CallStatus(
        NO_ONGOING_CALL, IDLE
    )

    private fun CallStatusDb.toDomain() = CallStatus(number, state.toCallState())

    private fun Int.toCallState() = when (this) {
        2 -> ONGOING
        1 -> RINGING
        else -> IDLE
    }
}
