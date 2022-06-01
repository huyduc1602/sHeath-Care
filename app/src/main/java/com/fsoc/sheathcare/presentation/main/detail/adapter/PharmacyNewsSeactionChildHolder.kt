package com.fsoc.sheathcare.presentation.main.detail.adapter

import android.view.View
import kotlinx.android.synthetic.main.item_pharmacy_news.view.*

class PharmacyNewsSeactionChildHolder(
    itemView: View,
    private val onItemClicked: ((position: Int) -> Unit)? = null
) : CommonSectionChildViewHolder(itemView) {


    fun onBind(item: String, position: Int) {
        itemView.setOnClickListener {
            onItemClicked?.invoke(position)
        }
        itemView.tvTitleCategory.text = item

//        onClickListener(item)
    }

    private fun onClickListener(item: String) {
//        itemView.tvTitleCategory.setOnClickListener { onItemClicked?.invoke(item) }
    }
}
