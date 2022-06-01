package com.fsoc.sheathcare.presentation.main.detail.infordapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.InfoProduct
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.BaseOrderAdapter
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(
    var data: ArrayList<InfoProduct>,
    private val itemClick: (obj: InfoProduct) -> Unit
) :
    BaseOrderAdapter<InfoProduct>(
        data,
        R.layout.item_search,
        R.layout.item_search
    ) {
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: InfoProduct) {
        view.apply {
            txtSearch.text = obj.name
        }
    }

    override fun onItemClick(obj: InfoProduct) {
        itemClick.invoke(obj)
    }

    override fun onItemLongClick(obj: InfoProduct) {
    }

    fun updateData(item : ArrayList<InfoProduct>){
        this.data = item
        notifyDataSetChanged()
    }
}