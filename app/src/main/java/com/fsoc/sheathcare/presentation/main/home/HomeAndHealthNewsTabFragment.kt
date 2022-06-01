package com.fsoc.sheathcare.presentation.main.home

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.CommonPagerAdapter
import com.fsoc.sheathcare.presentation.stamp.ItemTabBar
import kotlinx.android.synthetic.main.fragment_product_list_and_news_tab.*

class HomeAndHealthNewsTabFragment : BaseFragment<SlideVideoViewModel>() {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private lateinit var homeFragment: HomeFragment
    private lateinit var healthNewsFragment: HealthNewsFragment
    private lateinit var mVpRankingAdapter: CommonPagerAdapter
    private var mFragments: java.util.ArrayList<Fragment> = java.util.ArrayList()
    private lateinit var mHome: ItemTabBar
    private lateinit var mNewsProduct: ItemTabBar

    companion object {
        const val TYPE_TAB_HOME = 1
        const val TYPE_TAB_NEWS_PRODUCT = 0
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_product_list_and_news_tab
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SlideVideoViewModel::class.java)
    }

    override fun setUpView() {
        initView()
    }

    private fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        homeFragment =
            HomeFragment.newInstance(TYPE_TAB_HOME)
        mFragments.add(homeFragment)

        healthNewsFragment =
            HealthNewsFragment.newInstance(TYPE_TAB_NEWS_PRODUCT)
        mFragments.add(healthNewsFragment)

        mVpRankingAdapter = CommonPagerAdapter(childFragmentManager, mFragments)
        product_and_news_pager.apply {
            offscreenPageLimit = 4
            adapter = mVpRankingAdapter
//            currentItem = childPageOpen
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    val view = product_and_news_pager.getChildAt(position)
                    val height = view.measuredHeight
                    val customHeight =
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                    val layoutParam = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        customHeight
                    )
                    product_and_news_pager.layoutParams = layoutParam
                }
            })
        }
        setupTabLayout()
    }

    private fun setupTabLayout() {
        tabs_product_and_news.setupWithViewPager(product_and_news_pager)
        val countryTabTitle = resources.getString(R.string.home_title_tab)
        mHome = ItemTabBar(context, R.layout.item_tabs)
        mHome.setData(countryTabTitle, false)

        val inTheWorldTabTitle = resources.getString(R.string.health_new_tab)
        mNewsProduct = ItemTabBar(context, R.layout.item_tabs)
        mNewsProduct.setData(inTheWorldTabTitle, false)

        tabs_product_and_news.getTabAt(0)?.customView = mHome
        tabs_product_and_news.getTabAt(1)?.customView = mNewsProduct
    }

    override fun observable() {
        // do nothing
    }

    override fun fireData() {
        // do nothing
    }
}