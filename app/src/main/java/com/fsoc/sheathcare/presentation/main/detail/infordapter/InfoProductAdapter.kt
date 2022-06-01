package com.fsoc.sheathcare.presentation.main.detail.infordapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.InfoProduct
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.BaseOrderAdapter
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.item_info_product.view.*

class InfoProductAdapter(
    data: ArrayList<InfoProduct>,
    private val itemClick: (obj: InfoProduct) -> Unit
) :
    BaseOrderAdapter<InfoProduct>(
        data,
        R.layout.item_info_product,
        R.layout.item_info_product
    ) {
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: InfoProduct) {
        view.apply {
            iv_info_product.loadImage(obj.image)
            tv_name_info_product.text = obj.name
            rating_bar.rating = obj.likeNumber.toFloat()
        }
    }

    override fun onItemClick(obj: InfoProduct) {
        itemClick.invoke(obj)
    }

    override fun onItemLongClick(obj: InfoProduct) {
//        itemLongClick.invoke(obj)
    }
}