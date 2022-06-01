package com.fsoc.sheathcare.common.extension

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fsoc.sheathcare.common.extension.Constans.Companion.REQUEST_CODE_LOCATION
import com.fsoc.sheathcare.common.extension.Constans.Companion.SDK_VERSION_ANDROID_P

fun Activity.checkRequiredPermissions(): Boolean {
    val permission = if (Build.VERSION.SDK_INT <= SDK_VERSION_ANDROID_P) {
        Manifest.permission.ACCESS_COARSE_LOCATION
    } else {
        Manifest.permission.ACCESS_FINE_LOCATION
    }
    val statusPermission = ContextCompat.checkSelfPermission(this, permission)
    return if (statusPermission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_LOCATION)
        false
    } else {
        true
    }
}

fun Fragment.checkRequiredPermissions(): Boolean {
    val permission = if (Build.VERSION.SDK_INT <= SDK_VERSION_ANDROID_P) {
        Manifest.permission.ACCESS_COARSE_LOCATION
    } else {
        Manifest.permission.ACCESS_FINE_LOCATION
    }
    val statusPermission = ContextCompat.checkSelfPermission(this.requireContext(), permission)
    return if (statusPermission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(permission), REQUEST_CODE_LOCATION)
        false
    } else {
        true
    }
}
