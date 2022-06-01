package com.fsoc.sheathcare.presentation.main.detail.adapter

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.ProductCategoryModel
import com.fsoc.sheathcare.presentation.widget.StickyHeaderItemDecoration

class PharmacyNewsSeactionHeaderStickyHolder(itemView: View) :
    StickyHeaderItemDecoration.StickyHeaderViewHolder(itemView) {


    lateinit var tvTitle: TextView

    lateinit var imvDropDown: AppCompatImageView

    fun onBind(pharmacyNewsItem: ProductCategoryModel, isExpanded: Boolean) {
        val builder = SpannableStringBuilder()
//        val dateSpannable = SpannableString(pharmacyNewsItem.title)
//        builder.append(dateSpannable)
//        builder.append("  ")
//        val count = String.format(
//            getString(R.string.AP7000_total_news),
//            pharmacyNewsItem.listPharmacyNews.firstOrNull()?.totalByDate ?: 0
//        )
//        val countSpannable = SpannableString(count)
//        countSpannable.setSpan(
//            ForegroundColorSpan(getColor(R.color.dr_text_content)),
//            0,
//            count.length,
//            0
//        )
//        builder.append(countSpannable)
//        tvGroupDate.text = pharmacyNewsItem.name
        onSectionExpandedChanged(isExpanded)
    }

    private fun onSectionExpandedChanged(expanded: Boolean) {
        imvDropDown.apply {
            if (visibility == View.INVISIBLE) return
            setImageResource(if (expanded) R.drawable.ic_arrow_up_orange else R.drawable.ic_arrow_down_orange)
        }
    }
}
