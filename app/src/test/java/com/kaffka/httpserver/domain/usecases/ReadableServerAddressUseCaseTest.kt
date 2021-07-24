package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.ServerAddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ReadableServerAddressUseCaseTest {

    private val serverAddressRepository: ServerAddressRepository = mockk {
        coEvery { getWifiIP() } returns ipAddress
        coEvery { serverPort } returns serverPortMock
    }

    private val readableCallLogUseCase = ReadableServerAddressUseCase(serverAddressRepository)

    @Test
    fun `when getReadableAddress is called, return the formatted server address`() {
        val expectedAddress = "$ipAddress:$serverPortMock"
        runBlocking {
            val result = readableCallLogUseCase.getReadableAddress()

            assertEquals(expectedAddress, result)

            coVerify { serverAddressRepository.getWifiIP() }
            coVerify { serverAddressRepository.serverPort }
        }
    }
}
