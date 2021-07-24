package com.kaffka.httpserver.ui

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.kaffka.httpserver.R
import com.kaffka.httpserver.domain.ServerStatus
import com.kaffka.httpserver.domain.usecases.ReadableServerAddressUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.server.engine.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CHANNEL_ID = "ServerForegroundChannel"

@AndroidEntryPoint
class ServerService : Service() {

    @Inject
    lateinit var server: ApplicationEngine

    @Inject
    lateinit var address: ReadableServerAddressUseCase

    @Inject
    lateinit var status: MutableLiveData<ServerStatus>

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, getString(R.string.service_starting), Toast.LENGTH_LONG).show()
        createNotificationChannel()
        startServer(startId)
        startForeground(51, getNotification(address.getReadableAddress(), getPendingIntent()))
        status.postValue(ServerStatus.ONLINE)
        return START_NOT_STICKY
    }

    private fun getPendingIntent() =
        Intent(this, MainActivity::class.java).let {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    private fun getNotification(
        ipAddress: String?,
        pendingIntent: PendingIntent
    ): Notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(getText(R.string.notification_title))
        .setContentText(getString(R.string.notification_message, ipAddress))
        .setSmallIcon(R.drawable.ic_baseline_log)
        .setContentIntent(pendingIntent)
        .setPriority(IMPORTANCE_DEFAULT)
        .setTicker(getText(R.string.notification_title))
        .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startServer(startId: Int) {
        scope.launch(dispatcher) {
            server.start(wait = true)
            stopSelf(startId)
        }
    }

    private fun stopServer() {
        scope.launch(dispatcher) {
            server.stop(0, 0)
            status.postValue(ServerStatus.OFFLINE)
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        stopServer()
        Toast.makeText(this, getString(R.string.service_stopped), Toast.LENGTH_LONG).show()
    }
}
