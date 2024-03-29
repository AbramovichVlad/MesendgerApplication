package com.example.mesendgerapplication.utilities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
const val WRITE_FILE = Manifest.permission.WRITE_EXTERNAL_STORAGE
const val PERMISION_REQUEST = 200

fun checkPremision(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISION_REQUEST)
        false
    } else true
}