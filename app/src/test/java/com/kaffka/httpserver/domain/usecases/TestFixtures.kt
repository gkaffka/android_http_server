package com.kaffka.httpserver.domain.usecases

import com.kaffka.httpserver.domain.Call
import com.kaffka.httpserver.domain.CallState
import com.kaffka.httpserver.domain.CallStatus
import com.kaffka.httpserver.domain.CallType
import java.util.*

const val number = "123456"
const val timesQueried = 5
const val ipAddress = "192.192.192.19"
const val serverPortMock = 8000
val call = Call(Date(500), 10, number, "Gabriel", CallType.OUTGOING, 0)
val calls = listOf(call)
val callStatus = CallStatus(number, CallState.RINGING)
