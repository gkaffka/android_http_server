package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.domain.CallState
import com.kaffka.httpserver.domain.CallStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ReadableCallLogUseCaseTest {

    private val callLogUseCase: CallLogUseCase = mockk {
        coEvery { getCallLog() } returns calls
    }

    private val readableCallLogUseCase = ReadableCallLogUseCase(callLogUseCase)

    @Test
    fun `when getCallLog is called, get the human readable call data`() {
        val formattedCallData =
            """|start time: Thu Jan 01 01:00:00 CET 1970
               |duration: 10
               |number: 123456
               |name: Gabriel
               |type: OUTGOING
               |time queried: 0""".trimMargin()

        runBlocking {
            val result = readableCallLogUseCase.getCallLog()

            assertEquals(formattedCallData, result.first())
            coVerify { callLogUseCase.getCallLog() }
        }
    }

    private fun getCallStatus() = CallStatus(number, CallState.RINGING)
}
