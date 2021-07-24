package com.kaffka.httpserver.data

import com.kaffka.httpserver.data.local.ServerLogDao
import com.kaffka.httpserver.domain.ServerLog
import javax.inject.Inject
import com.kaffka.httpserver.data.local.ServerLog as OngoingCallLogDB

class ServerLogRepository @Inject constructor(private val serverLogDao: ServerLogDao) {

    suspend fun getTimesQueriedCount(number: String) =
        serverLogDao.getServerLog().firstOrNull { it.number == number }?.timesQueried

    suspend fun getServerLog() = serverLogDao.getServerLog().map { it.toDomain() }

    suspend fun insertServerLog(serverLog: ServerLog) =
        serverLogDao.insertServerLog(serverLog.toDB())

    private fun ServerLog.toDB() = OngoingCallLogDB(number, timesQueried)

    private fun OngoingCallLogDB.toDomain() = ServerLog(number, timesQueried)

}
