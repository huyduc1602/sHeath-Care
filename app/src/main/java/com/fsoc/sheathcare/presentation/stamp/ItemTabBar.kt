package com.fsoc.sheathcare.presentation.stamp

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.utils.DimensionUtils
import kotlinx.android.synthetic.main.item_tab_bar.view.*

class ItemTabBar(context: Context?, resourceLayout: Int? = null) : RelativeLayout(context) {

    companion object {
        val WIDTH_ATTENDANCE_ICON_TAB = DimensionUtils.dpToPx(16F).toInt()
        val WIDTH_ATTENDANCE_MARGIN_ICON_TAB = DimensionUtils.dpToPx(2F).toInt()
    }

//    @BindView(R.id.iconIv)
//    @JvmField
//    var imvIcon: AppCompatImageView? = nullg
//
//    @BindView(R.id.tv_title)
//    @JvmField
//    var tvTitle: TextView? = null
//
//    @BindView(R.id.iv_menu_tab)
//    @JvmField
//    var ivMenuTab: AppCompatImageView? = null

    private var title: String? = null
    private var iconDrawable: Int = 0

    var visibleRedDot: Boolean = false
        set(value) {
            field = value
            if (iconIv != null) {
                iconIv!!.visibility = if (value) View.VISIBLE else View.GONE
            }
        }

    private var isVisibleImageMenuTab: Boolean = false
        set(value) {
            field = value
            iv_menu_tab?.visibility = if (value) View.VISIBLE else View.GONE
            iv_menu_tab?.layoutParams?.width = if (value) WIDTH_ATTENDANCE_ICON_TAB else 0
            iv_menu_tab?.requestLayout()

            tv_title?.layoutParams?.width =
                if (value) LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT
            tv_title?.requestLayout()
        }

    fun setValueForImageMenuTab(resId: Int?) {
        resId?.let {
            isVisibleImageMenuTab = true
            iv_menu_tab?.setImageResource(it)
        }
    }

    fun getTitle(): String {
        return title ?: ""
    }

    fun setTextSize(textSize: Float) {
        tv_title?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setData(title: String, iconDrawable: Int) {
        this.title = title
        this.iconDrawable = iconDrawable
        fillData()
    }

    fun setData(title: String, isDisplayRedDot: Boolean) {
        this.title = title
        visibleRedDot = isDisplayRedDot
        fillData()
    }

    init {
        val view = LayoutInflater
            .from(context)
            .inflate(resourceLayout ?: R.layout.item_tab_bar, this, false)
//        ButterKnife.bind(this, view)
        addView(view)
        isVisibleImageMenuTab = false
    }

    private fun fillData() {
        tv_title?.text = title
    }

}