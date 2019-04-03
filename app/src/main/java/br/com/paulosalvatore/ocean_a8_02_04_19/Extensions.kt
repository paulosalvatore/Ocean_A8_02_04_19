package br.com.paulosalvatore.ocean_a8_02_04_19

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v4.app.ActivityCompat.checkSelfPermission

fun Context.isNotPermissionGranted(permission: String) = checkSelfPermission(this, permission) != PERMISSION_GRANTED
