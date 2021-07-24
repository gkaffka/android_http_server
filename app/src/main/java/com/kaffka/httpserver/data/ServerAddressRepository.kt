package com.kaffka.httpserver.data

interface ServerAddressRepository {
    val serverPort : Int
    fun getWifiIP(): String
}
