package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.CallStatusRepository
import com.kaffka.httpserver.data.ServerLogRepository

import com.kaffka.httpserver.domain.CallState
import com.kaffka.httpserver.domain.CallStatus
import com.kaffka.httpserver.domain.ServerLog
import javax.inject.Inject

/**
 * Used to get call log status of a call and register the number of requests a given
 * number has it status requested
 */
class GetCallStatusUseCase @Inject constructor(
    private val statusRepository: CallStatusRepository,
    private val serverLogRepository: ServerLogRepository
) {
    suspend fun getCallStatus(): CallStatus {
        val status = statusRepository.getCallStatus()
        if (status.state != CallState.IDLE) {
            serverLogRepository.insertServerLog(getServerLog(status.number))
        }
        return status
    }

    private suspend fun getServerLog(number: String): ServerLog {
        val count = serverLogRepository.getTimesQueriedCount(number) ?: 0
        return ServerLog(number, count + 1)
    }
}
