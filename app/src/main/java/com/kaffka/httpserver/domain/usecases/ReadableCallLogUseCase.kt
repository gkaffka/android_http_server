package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.domain.Call
import javax.inject.Inject

/**
 * Used to get the logged calls as a formatted human readable string
 */
class ReadableCallLogUseCase @Inject constructor(
    private val callLogUseCase: CallLogUseCase
) {

    suspend fun getCallLog() = callLogUseCase.getCallLog().formatted()

    private fun List<Call>.formatted() =
        map {
            "start time: ${it.startDate}" +
                    "\nduration: ${it.duration}" +
                    "\nnumber: ${it.number}" +
                    "\nname: ${it.name}" +
                    "\ntype: ${it.type}" +
                    "\ntime queried: ${it.timesQueried}"
        }
}
