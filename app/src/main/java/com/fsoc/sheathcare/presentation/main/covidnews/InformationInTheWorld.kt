package com.fsoc.sheathcare.presentation.main.covidnews

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModelItemModel
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.CovidInTheWorldAdapter
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_information_in_the_world.*

class InformationInTheWorld : BaseFragment<CovidNewsViewModel>() {
    private val mData = arrayListOf<CovidInTheWorldModelItemModel>()
    var typeTab = 0

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(typeTab: Int): InformationInTheWorld {
            val ret = InformationInTheWorld()
            val b = Bundle()
            ret.arguments = b
            ret.typeTab = typeTab
            return ret
        }
    }

    private val mAdapter: CovidInTheWorldAdapter by lazy {
        CovidInTheWorldAdapter(mData) {
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_information_in_the_world
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CovidNewsViewModel::class.java)
    }

    override fun setUpView() {
        toolbarFragment.show(false)
        initList()
    }

    private fun initList() {
        rcvListCovidInTheWorld.apply {
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

    override fun observable() {
        observe(viewModel.covidListInTheWorld) {
            mData.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun fireData() {
        viewModel.getListCovidNewsInTheWorld()
    }
}