package com.fsoc.sheathcare.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.RequiresApi


/**
 * Created by Tuha on 8/25/2017.
 */
object DimensionUtils {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun dpToPx(dp: Float): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    fun pxToDp(px: Float): Float {
        return (px / Resources.getSystem().displayMetrics.density)
    }

    /**
     * This method converts devices specific px to sp independent on scaleDensity
     * @param sp A value in sp unit of fontSize. Which we need to convert
     * @return A float value to represent px equivalent to sp value
     */
    fun spToPx(sp: Float): Float {
        return (sp * (Resources.getSystem().displayMetrics.scaledDensity))
    }

    /**
     * Return the width of screen in px
     */
    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    /**
     * Return the height of screen in px
     */
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    /**
     * Return the height of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getNavigationBarHeight(activity: Activity): Int {
        val display = activity.windowManager?.defaultDisplay
        return if (display == null) {
            0
        } else {
            val realMetrics = DisplayMetrics()
            display.getRealMetrics(realMetrics)
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            realMetrics.heightPixels - metrics.heightPixels
        }
    }
    /**
     * Return the height of status bar in px
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

}

fun Float.dp2Px(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()
}