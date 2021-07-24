package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.CallRepository
import com.kaffka.httpserver.data.ServerLogRepository
import com.kaffka.httpserver.domain.Call
import com.kaffka.httpserver.domain.ServerLog
import javax.inject.Inject

/**
 * Used to get the logged calls as a list of calls tht can later be serialized
 */
class CallLogUseCase @Inject constructor(
    private val callRepository: CallRepository,
    private val serverLogRepository: ServerLogRepository
) {

    suspend fun getCallLog(): List<Call> {
        val logs = serverLogRepository.getServerLog()
        return callRepository.getFreshCallLogs().map {
            it.copy(timesQueried = logs.getTimesQueried(it.number))
        }
    }

    private fun List<ServerLog>.getTimesQueried(number: String) =
        this.firstOrNull { number == it.number }?.timesQueried ?: 0
}
