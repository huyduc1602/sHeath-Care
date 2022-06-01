package com.fsoc.sheathcare.presentation.main.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View


open class CommonSectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mClickCallback: ((holder: CommonSectionHeaderViewHolder, position: Int) -> Unit)? = null

    var sectionIndex: Int = 0

    init {
        itemView.setOnClickListener { mClickCallback?.invoke(this, adapterPosition) }
    }

    @Suppress("Don't use this method")
    fun setOnClickSectionListener(cb: (holder: CommonSectionHeaderViewHolder, position: Int) -> Unit): CommonSectionHeaderViewHolder {
        mClickCallback = cb
        return this
    }

    @Suppress("Unused")
    open fun onSectionExpandedChanged(sectionIndex: Int, expanded: Boolean) {

    }
}