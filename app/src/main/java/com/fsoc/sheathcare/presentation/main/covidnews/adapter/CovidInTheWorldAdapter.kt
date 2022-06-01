package com.fsoc.sheathcare.presentation.main.covidnews.adapter

import android.view.View
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModelItemModel
import kotlinx.android.synthetic.main.item_list_covid.view.*
import kotlinx.android.synthetic.main.item_list_covid_in_the_world.view.*

class CovidInTheWorldAdapter(
    data: List<CovidInTheWorldModelItemModel>,
    private val itemClick: (obj: CovidInTheWorldModelItemModel) -> Unit
) :
    BaseOrderAdapter<CovidInTheWorldModelItemModel>(
        data,
        R.layout.item_list_covid_in_the_world,
        R.layout.item_list_covid_in_the_world
    ) {
    override fun getViewType(position: Int): ViewType {
        return ViewType.CONTENT
    }

    override fun bindItemView(view: View, obj: CovidInTheWorldModelItemModel) {
        view.apply {
            tv_name_in_the_world.text = obj.country
            tv_confirmed_stat_in_the_world.text = obj.cases.toString()
            tv_recovery_stat_in_the_world.text = obj.recovered.toString()
            tv_deaths_stat_in_the_world.text = obj.deaths.toString()
        }
    }

    override fun onItemClick(obj: CovidInTheWorldModelItemModel) {
        itemClick.invoke(obj)
    }

    override fun onItemLongClick(obj: CovidInTheWorldModelItemModel) {
//        itemLongClick.invoke(obj)
    }
}