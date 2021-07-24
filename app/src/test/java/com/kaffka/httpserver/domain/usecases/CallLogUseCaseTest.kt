package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.CallRepository
import com.kaffka.httpserver.data.ServerLogRepository
import com.kaffka.httpserver.domain.ServerLog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CallLogUseCaseTest {

    private val serverLog = listOf(ServerLog(number, timesQueried))

    private val callRepository: CallRepository = mockk {
        coEvery { getFreshCallLogs() } returns calls
    }

    private val serverLogRepository: ServerLogRepository = mockk {
        coEvery { getServerLog() } returns serverLog
    }
    private val callLogUseCase = CallLogUseCase(callRepository, serverLogRepository)

    @Test
    fun `when getCallLog is called, enrich calls with the server log data and then return the enriched calls`() {
        runBlocking {
            val result = callLogUseCase.getCallLog()

            assertEquals(1, result.size)
            assertEquals(number, result.first().number)
            assertEquals(timesQueried, result.first().timesQueried)
            coVerify { callRepository.getFreshCallLogs() }
            coVerify { serverLogRepository.getServerLog() }
        }
    }

}
