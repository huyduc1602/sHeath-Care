package com.ominext.healthy.common.extension

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.presentation.MyApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImage(url: String?) {
    val option = RequestOptions.placeholderOf(R.drawable.image_default)
    Glide.with(this)
        .load(url)
        .apply(option)
        .into(this)
}

fun ImageView.loadThumb(url: String?) {
    val option = RequestOptions.placeholderOf(R.drawable.image_default)
    Glide.with(this)
        .load(url)
        .centerCrop()
        .circleCrop()
        .thumbnail(0.1f)
        .override(ImageExtension.THUMBNAIL_SIZE, ImageExtension.THUMBNAIL_SIZE)
        .apply(option)
        .into(this)
}

object ImageExtension {
    const val THUMBNAIL_SIZE = 128
    private const val DIR_OUTPUT = "sHeathCare"


    fun createImageFile(
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        suffix: String? = null
    ): File? {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
        val now = Date()
        val fileName: String = formatter.format(now).toString() + ".jpg"
        return getOutputImageFile(fileName = fileName)
    }

    fun createImageFile(): File? {
        return createImageFile(format = Bitmap.CompressFormat.JPEG, suffix = null)
    }

    fun getOutputImageFile(fileName: String, suffix: String? = null): File? {
        var nameFileFull = fileName
        if (suffix != null) {
            nameFileFull = "$fileName$suffix"
        }
        return getOutputFile(Environment.DIRECTORY_PICTURES, nameFileFull)
    }

    fun getOutputFile(type: String, fileName: String): File? {
        var file: File? = null
        // We can read and write media, Check externalStorage available
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            var dir: File? = File(MyApplication.instance.getExternalFilesDir(type), DIR_OUTPUT)
            if (dir != null) {
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        dir = null
                    }
                }
                if (dir != null) {
                    file = File(dir.path + File.separator + fileName)
                }
            }
        }
        return file
    }

    fun deleteFileImageWithUri(context: Context?, uri: Uri?) {
        if (uri != null) {
            try {
                val fileDelete = File(uri.path)
                if (fileDelete.exists()) {
                    fileDelete.delete()
                } else {
                    // Remove the file with gallery
                    context?.contentResolver?.delete(uri, null, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

