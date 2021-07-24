package com.kaffka.httpserver.data

import com.kaffka.httpserver.domain.Routes
import com.kaffka.httpserver.domain.Service
import javax.inject.Inject


class ServicesRepository @Inject constructor(private val serverAddressRepository: ServerAddressRepository) {

    fun getServices(): List<Service> = buildService(Routes.LOG, Routes.STATUS)

    private fun buildService(vararg route: Routes) = MutableList(route.size) {
        Service(
            route[it].value,
            buildURlFromIP(
                route[it].value,
                serverAddressRepository.getWifiIP(),
                serverAddressRepository.serverPort
            )
        )
    }

    private fun buildURlFromIP(path: String, ipAddress: String, port: Int) =
        "http://$ipAddress:$port/$path"
}
