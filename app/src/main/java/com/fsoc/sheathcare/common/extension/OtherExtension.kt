package com.fsoc.sheathcare.common.extension

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.URLUtil
import androidx.fragment.app.Fragment

fun Fragment.openBrowser(url: String) {
    // avoid crash for invalid url case
    if (!URLUtil.isValidUrl(url)) {
        return
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}

fun Fragment.openChrome(url: String) {
    if (!URLUtil.isValidUrl(url)) {
        return
    }
    val intent = Intent(Intent.ACTION_VIEW)
    if (this.context?.isPackageInstalled("com.android.chrome") == true) {
        intent.setPackage("com.android.chrome")
    }
    intent.data = Uri.parse(url)
    startActivity(intent)
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
fun Fragment.call(phone: String?) {
    val phoneTmp = phone?.replace(" ", "")?.replace("-", "")?.replace("–", "")
    if (phoneTmp.isNumber()) {
        val intentCall = Intent()
        intentCall.action = Intent.ACTION_DIAL
        intentCall.data = Uri.parse("tel:$phone")
        startActivity(intentCall)
    }
}

fun String?.isNumber(): Boolean {
    return this?.toLongOrNull() != null
}

fun Int.isEven(): Boolean {
    return this % 2 == 0
}
