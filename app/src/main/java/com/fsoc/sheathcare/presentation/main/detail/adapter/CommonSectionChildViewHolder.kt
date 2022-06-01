package com.fsoc.sheathcare.presentation.main.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View


open class CommonSectionChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mClickCallback: ((holder: CommonSectionChildViewHolder, position: Int) -> Unit)? = null

    var sectionIndex: Int = 0

    var childIndex: Int = 0

    init {
        itemView.setOnClickListener { mClickCallback?.invoke(this, adapterPosition) }
    }

    @Suppress("Don't use this method")
    fun setOnClickChildListener(cb: (holder: CommonSectionChildViewHolder, position: Int) -> Unit): CommonSectionChildViewHolder {
        mClickCallback = cb
        return this
    }

    @Suppress("Unused")
    open fun onChildClicked(sectionIndex: Int, childIndex: Int) {

    }
}