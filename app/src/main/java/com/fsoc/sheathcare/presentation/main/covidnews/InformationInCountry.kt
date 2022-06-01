package com.fsoc.sheathcare.presentation.main.covidnews

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.GlobalModel
import com.fsoc.sheathcare.domain.entity.ProvinceModel
import com.fsoc.sheathcare.domain.entity.VietnamModel
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.CovidNewAdapter
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_information_country.*


class InformationInCountry : BaseFragment<CovidNewsViewModel>() {
    private val mData = arrayListOf<ProvinceModel>()
    private var mFireData = true
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private val mAdapter: CovidNewAdapter by lazy {
        CovidNewAdapter(mData) {
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(typeTab: Int): InformationInCountry {
            val ret = InformationInCountry()
            val b = Bundle()
            ret.arguments = b
            ret.typeTab = typeTab
            return ret
        }
    }

    var typeTab = 0
    override fun layoutRes(): Int {
        return R.layout.fragment_information_country
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CovidNewsViewModel::class.java)
    }

    override fun setUpView() {
        toolbarFragment.show(false)
        initList()
    }

    override fun observable() {
        observe(viewModel.covidList) {
            fillDataUI(it.data.vietnam)
            fillDataUI1(it.data.global)
            mData.addAll(it.data.vietnam.listProvince)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun fillDataUI(obj: VietnamModel) {
        tv_number_of_infections.text =
            String.format(getString(R.string.number_of_infections), obj.confirmed)
        tv_infection.text = String.format(getString(R.string.infections), obj.confirmed)
        tv_recover.text = String.format(getString(R.string.recover), obj.recovered)
        tv_deaths1.text = String.format(getString(R.string.deaths), obj.deaths)
    }

    private fun fillDataUI1(obj: GlobalModel) {
        tv_confirmed.text = obj.confirmed.toString()
        tv_deaths.text = obj.deaths.toString()
        tv_recovered.text = obj.recovered.toString()
    }

    /**
     * This is the method init list staff at the same time set up Load Endless
     */
    private fun initList() {
        rcvListCovid.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            isNestedScrollingEnabled = false
//            setupLoadEndless(mCurrentPage) { page: Int, totalItemsCount: Int ->
//                Logger.d("page: $page count: $totalItemsCount")
//                mCurrentPage = page
//                if (mCurrentPage < mTotalPage) {
//                    mIsLoadMore = true
//                    fireData()
//                }
//            }
        }
    }

    override fun fireData() {
        if (!mFireData) {
            mFireData = true
            return
        }
        viewModel.getListCovidNews()
    }
}