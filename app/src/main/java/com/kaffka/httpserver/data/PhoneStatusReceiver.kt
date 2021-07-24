package com.kaffka.httpserver.data

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhoneStatusReceiver : BroadcastReceiver() {

    @Inject
    lateinit var callStatusRepository: CallStatusRepository

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    override fun onReceive(context: Context, intent: Intent) {
        val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private var listener: PhoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)
            updateData(incomingNumber, state)
        }
    }

    private fun updateData(incomingNumber: String, state: Int) {
        scope.launch(dispatcher) { callStatusRepository.updateStatus(incomingNumber, state) }
    }
}
