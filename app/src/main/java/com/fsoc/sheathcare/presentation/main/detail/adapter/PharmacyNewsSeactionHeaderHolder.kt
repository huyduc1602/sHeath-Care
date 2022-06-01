package com.fsoc.sheathcare.presentation.main.detail.adapter

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.fsoc.sheathcare.domain.entity.ProductCategoryModel
import kotlinx.android.synthetic.main.item_group_pharmacy_news.view.*

class PharmacyNewsSeactionHeaderHolder(itemView: View) : CommonSectionHeaderViewHolder(itemView) {
    lateinit var tvTitle: TextView

    lateinit var imvDropDown: AppCompatImageView

    fun onBind(pharmacyNewsItem: ProductCategoryModel) {
        val builder = SpannableStringBuilder()

//        val dateSpannable = SpannableString(pharmacyNewsItem.title)
//        builder.append(dateSpannable)
//        builder.append("  ")
//        val count = String.format(
//            getString(R.string.AP7000_total_news),
//            pharmacyNewsItem.listPharmacyNews.firstOrNull()?.totalByDate ?: 0
//        )
//        val countSpannable = SpannableString(count)
//        countSpannable.setSpan(ForegroundColorSpan(AppUtils.getColor(R.color.dr_text_content)), 0, count.length, 0)
//        builder.append(countSpannable)
//        tvGroupDate.setText(builder, TextView.BufferType.SPANNABLE)
            itemView.tvGroupDate.text = pharmacyNewsItem.name
    }

    override fun onSectionExpandedChanged(sectionIndex: Int, expanded: Boolean) {
        if (expanded) {
//            imvDropDown.setImageResource(R.drawable.ic_arrow_up_orange)
        } else {
//            imvDropDown.setImageResource(R.drawable.ic_arrow_down_orange)
        }
    }
}
