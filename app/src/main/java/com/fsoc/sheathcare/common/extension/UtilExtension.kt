package com.fsoc.sheathcare.common.extension

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.fsoc.sheathcare.R
import java.util.concurrent.Executors


fun Context.hideKeyboardFrom(view: View) {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard() {
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    this.hideKeyboardFrom(view)
}

fun Fragment.hideKeyboard() {
    context?.hideKeyboardFrom(requireView())
}

/**
 * Hide keyboard when User touch out site input is EditText  or search view
 */
fun Fragment.hideKeyBoardWhenTouchOutside() {
    view?.hideKeyBoardWhenTouchOutside()
}

fun View.hideKeyBoardWhenTouchOutside(viewFocus: View? = null) {
    if (this !is EditText) {
        this.setOnTouchListener { _, _ ->
            hideKeyBoard()
            viewFocus?.requestFocus()
            false
        }
    }
}

fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.getString(stringId: Int): String {
    return this.resources.getString(stringId)
}


private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun runOnIoThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

 fun Fragment.getMarkerBitmapFromStore(@DrawableRes resId: Int, context: Context): Bitmap? {
    val customMarkerView: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
        R.layout.item_view_stores_marker,
        null
    )
    val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image_stores) as ImageView
    markerImageView.setImageResource(resId)
    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
    customMarkerView.buildDrawingCache()
    val returnedBitmap = Bitmap.createBitmap(
        customMarkerView.measuredWidth, customMarkerView.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = customMarkerView.background
    drawable?.draw(canvas)
    customMarkerView.draw(canvas)
    return returnedBitmap
}