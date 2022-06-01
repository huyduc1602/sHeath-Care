package com.fsoc.sheathcare.presentation.main.covidnews.adapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.ProvinceModel
import kotlinx.android.synthetic.main.item_list_covid.view.*

class CovidNewAdapter(
    data: List<ProvinceModel>,
    private val itemClick: (obj: ProvinceModel) -> Unit
) :
    BaseOrderAdapter<ProvinceModel>(
        data,
        R.layout.item_list_covid,
        R.layout.item_list_covid
    ) {
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: ProvinceModel) {
        view.apply {
            tv_name.text = obj.name
            tv_confirmed_stat.text = obj.confirmed.toString()
            tv_recovery_stat.text = obj.recovered.toString()
            tv_deaths_stat.text = obj.deaths.toString()
        }
    }

    override fun onItemClick(obj: ProvinceModel) {
        itemClick.invoke(obj)
    }

    override fun onItemLongClick(obj: ProvinceModel) {
//        itemLongClick.invoke(obj)
    }
}