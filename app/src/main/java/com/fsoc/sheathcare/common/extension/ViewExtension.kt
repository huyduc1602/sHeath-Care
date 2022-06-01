package com.fsoc.sheathcare.common.extension

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fsoc.sheathcare.common.EndlessRecyclerViewScrollListener

fun ViewGroup.inflateView(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun RecyclerView.setupLoadEndless(
    currentPage: Int,
    block: (page: Int, totalItemsCount: Int) -> Unit
): EndlessRecyclerViewScrollListener {
    val onScrollListener =
        object :
            EndlessRecyclerViewScrollListener(currentPage, layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                block.invoke(page, totalItemsCount)
            }

        }
    addOnScrollListener(onScrollListener)
    return onScrollListener
}

fun View.show(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}


fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}


fun View.click(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = 200,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}