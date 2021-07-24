package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.CallStatusRepository
import com.kaffka.httpserver.data.ServerLogRepository
import com.kaffka.httpserver.domain.ServerLog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCallStatusUseCaseTest {

    private val callStatusRepository: CallStatusRepository = mockk {
        coEvery { getCallStatus() } returns callStatus
    }

    private val serverLogRepository: ServerLogRepository = mockk {
        coEvery { getTimesQueriedCount(number) } returns timesQueried
        coEvery { insertServerLog(any()) } returns Unit
    }
    private val getCallStatusUseCase =
        GetCallStatusUseCase(callStatusRepository, serverLogRepository)

    @Test
    fun `when getCallStatus is called, get the call status and increase the timesQueried counter for that number`() {
        runBlocking {
            val result = getCallStatusUseCase.getCallStatus()

            assertEquals(callStatus, result)
            coVerify { callStatusRepository.getCallStatus() }
            coVerify { serverLogRepository.insertServerLog(ServerLog(number, timesQueried + 1)) }
        }
    }

}
