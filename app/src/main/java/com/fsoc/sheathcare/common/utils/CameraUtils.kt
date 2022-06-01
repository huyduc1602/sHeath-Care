package com.fsoc.sheathcare.common.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.fsoc.sheathcare.BuildConfig
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ominext.healthy.common.extension.ImageExtension.createImageFile
import java.io.File
import java.io.IOException
import java.util.*

object CameraUtils {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private val currentUserRef: StorageReference
        get() = storageInstance.reference
            .child(
                MainActivity.user.email
                ?: throw NullPointerException("UID is null."))

    const val REQUEST_ID_IMAGE_CAPTURE = 100
    fun openGallery(fragment: Fragment, isOpenVideo: Boolean = false, requestCode: Int) {
        val intent: Intent
        if (isOpenVideo) {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.type = "video/*"
        } else {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
        }
        if (fragment.activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    fun openGallery(activity: Activity, isOpenVideo: Boolean = false, requestCode: Int) {
        val intent: Intent
        if (isOpenVideo) {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.type = "video/*"
        } else {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
        }
        if (activity.packageManager?.let { intent.resolveActivity(it) } != null) {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    fun openCamera(activity: Activity, requestCode: Int = REQUEST_ID_IMAGE_CAPTURE): File? {
        var photoFile: File? = null
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (activity.packageManager?.let { takePictureIntent.resolveActivity(it) } != null) {
            try {
                // Create the File where the photo should go
                photoFile = createImageFile()
            } catch (e: IOException) {
                // Error occurred while creating the File
                e.printStackTrace()
            }
            // Continue only if the File was successfully created
            val photoURI: Uri
            if (photoFile != null) {
                // FileProvider required for Android 7.  Sending a file URI throws exception.
                photoURI = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        activity.applicationContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile
                    )
                } else {
                    // #14161- For older devices
                    Uri.fromFile(photoFile)
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }
        return photoFile
    }
    fun getFileNameFull(context: Context, returnUri: Uri?): String {
        var fileName = ""

        if (returnUri != null) {
            when {
                returnUri.scheme == ContentResolver.SCHEME_CONTENT -> {
                    val returnCursor: Cursor? = context.contentResolver.query(returnUri, arrayOf(
                        OpenableColumns.DISPLAY_NAME), null, null, null)
                    if (returnCursor?.moveToFirst() == true) {
                        returnCursor.moveToFirst()
                        fileName = returnCursor.getString(0)
                    }
                    returnCursor?.close()
                }
                returnUri.scheme == ContentResolver.SCHEME_FILE -> {
                    val filePath = returnUri.path
                    if (!TextUtils.isEmpty(filePath)) {
                        if (filePath != null) {
                            fileName = filePath.substring(filePath.lastIndexOf("/") + 1)
                        }
                    }
                }
            }
        }
        return fileName
    }
    fun uploadMessageImage(imageBytes: ByteArray,
                           onSuccess: (imagePath: String) -> Unit) {
        val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    onSuccess(url.toString())

                }
            }
    }
    fun pathToReference(path: String) = storageInstance.getReference(path)

}