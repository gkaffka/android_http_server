package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.data.ServerAddressRepository
import javax.inject.Inject

/**
 * Used to get the human readable Server Address
 */

class ReadableServerAddressUseCase @Inject constructor(private val serverAddressRepository: ServerAddressRepository) {

    fun getReadableAddress() =
        "${serverAddressRepository.getWifiIP()}:${serverAddressRepository.serverPort}"
}
