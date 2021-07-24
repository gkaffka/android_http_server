package com.kaffka.httpserver.domain

import java.util.*

data class Call(
    val startDate: Date,
    val duration: Long,
    val number: String,
    val name: String?,
    val type: CallType,
    val timesQueried: Int?
)

data class Service(
    val name: String,
    val url: String
)

data class CallStatus(
    val number: String,
    val state: CallState
)

data class ServerLog(
    val number: String,
    val timesQueried: Int
)

enum class CallState {
   RINGING, ONGOING, IDLE
}

enum class CallType {
    INCOMING, OUTGOING, MISSED, VOICEMAIL, REJECTED, BLOCKED, UNKNOWN
}

enum class ServerStatus {
    ONLINE, OFFLINE
}
