package com.kaffka.httpserver.ui

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.kaffka.httpserver.R
import dagger.hilt.android.AndroidEntryPoint


private const val PERMISSION_REQUEST = 42

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            requestPermission()
        }
    }

    private fun startFragment() {
        supportFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            add<ServerControlFragment>(R.id.fragment_container_view)
        }
    }


    private fun requestPermission() {
        if (this.isGranted(READ_CALL_LOG) && this.isGranted(READ_PHONE_STATE)) {
            startFragment()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_CALL_LOG, READ_PHONE_STATE),
                PERMISSION_REQUEST
            );

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST && grantResults.filterNot { it == PERMISSION_GRANTED }
                .isEmpty()) {
            startFragment()
        } else {
            Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_LONG).show()
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun Context.isGranted(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
}
