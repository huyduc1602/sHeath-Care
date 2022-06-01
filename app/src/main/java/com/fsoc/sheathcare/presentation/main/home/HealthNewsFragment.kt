package com.fsoc.sheathcare.presentation.main.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.HealthNewsModel
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.home.adapter.HealthNewsAdapter
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_news_product.*

class HealthNewsFragment : BaseFragment<SlideVideoViewModel>() {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(typeTab: Int): HealthNewsFragment {
            val ret = HealthNewsFragment()
            val b = Bundle()
            ret.arguments = b
            ret.typeTab = typeTab
            return ret
        }
    }

    var typeTab = 0
    private val mData = arrayListOf<HealthNewsModel>()
    private val mAdapter: HealthNewsAdapter by lazy {
        HealthNewsAdapter(mData) {
            onClickItemHealthNews(it)
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_news_product
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(activity ?: return, viewModelFactory)
            .get(SlideVideoViewModel::class.java)
    }

    override fun setUpView() {
        toolbarFragment.show(false)
        initList()
    }

    private fun initList() {
        rcv_health_news.apply {
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
        observe(viewModel.listHealthNews) {
            mData.clear()
            mData.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun fireData() {
        viewModel.getListHealthNews()
    }

    private fun onClickItemHealthNews(obj: HealthNewsModel) {
        val arg = Bundle().apply {
            putString(DetailInformationHealthNewsFragment.HEALTH_NEWS_IMAGE, obj.imageNews)
            putString(DetailInformationHealthNewsFragment.HEALTH_NEWS_PREVENTION, obj.prevention)
            putString(DetailInformationHealthNewsFragment.HEALTH_NEWS_SYMPTOM, obj.symptom)
        }
        navigate(R.id.detailInfoHealthNewsFragment, arg)

    }
}