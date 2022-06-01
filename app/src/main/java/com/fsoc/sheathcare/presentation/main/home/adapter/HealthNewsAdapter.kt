package com.fsoc.sheathcare.presentation.main.home.adapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.HealthNewsModel
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.BaseOrderAdapter
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.item_list_health_news.view.*

class HealthNewsAdapter(
    data: List<HealthNewsModel>,
    private val itemClick: (obj: HealthNewsModel) -> Unit
) :
    BaseOrderAdapter<HealthNewsModel>(
        data,
        R.layout.item_list_health_news,
        R.layout.item_list_health_news
    ) {
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: HealthNewsModel) {
        view.apply {
            tv_title_news.text = obj.titleNews
            tv_createAt.text = obj.createAt
            iv_health_news.loadImage(obj.imageNews)
        }
    }

    override fun onItemClick(obj: HealthNewsModel) {
        itemClick.invoke(obj)
    }

    override fun onItemLongClick(obj: HealthNewsModel) {
//        itemLongClick.invoke(obj)
    }
}