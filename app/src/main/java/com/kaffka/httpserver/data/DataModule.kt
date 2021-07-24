package com.kaffka.httpserver.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.kaffka.httpserver.data.local.CallDao
import com.kaffka.httpserver.data.local.CallStatusDao
import com.kaffka.httpserver.data.local.LocalDatabase
import com.kaffka.httpserver.data.local.ServerLogDao
import com.kaffka.httpserver.domain.ServerStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providesCallDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java, "call_log_database"
        ).build()

    @Provides
    fun providesCallDao(localDatabase: LocalDatabase): CallDao = localDatabase.callDao()

    @Provides
    fun providesCallStatusDao(localDatabase: LocalDatabase): CallStatusDao =
        localDatabase.callStatusDao()

    @Provides
    fun providesOngoingCallDao(localDatabase: LocalDatabase): ServerLogDao =
        localDatabase.serverLogDao()

    @Provides
    fun providesContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    fun providesIPRepository(@ApplicationContext context: Context): ServerAddressRepository =
        object : ServerAddressRepository {
            @SuppressLint("WifiManagerPotentialLeak") // Already injecting the application context
            override fun getWifiIP(): String {
                val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
                return Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
            }

            override val serverPort = 8000
        }

    @Singleton
    @Provides
    fun providesServerStatus(): MutableLiveData<ServerStatus> =
        MutableLiveData(ServerStatus.OFFLINE)

    @Singleton
    @Provides
    fun providesServerStatusImmutable(liveData: MutableLiveData<ServerStatus>): LiveData<ServerStatus> =
        liveData
}
