package com.fsoc.sheathcare.presentation.main.covidnews

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.MainViewModel
import com.fsoc.sheathcare.presentation.main.covidnews.adapter.CommonPagerAdapter
import com.fsoc.sheathcare.presentation.stamp.ItemTabBar
import kotlinx.android.synthetic.main.fragment_covid_news.*

class CovidNewsFragment : BaseFragment<MainViewModel>() {

    private lateinit var informationInCountryFragment: InformationInCountry
    private lateinit var informationInTheWorldFragment: InformationInTheWorld
    private lateinit var mVpRankingAdapter: CommonPagerAdapter
    private var mFragments: java.util.ArrayList<Fragment> = java.util.ArrayList()

    private lateinit var mInTheWorld: ItemTabBar
    private lateinit var mCountry: ItemTabBar
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        const val TYPE_TAB_INFORMATION_COUNTRY = 1
        const val TYPE_TAB_INFORMATION_IN_THE_WORLD = 0
    }


    override fun layoutRes(): Int {
        return R.layout.fragment_covid_news
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

    }

    override fun setUpView() {
        (activity as MainActivity).initView()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Tin tức Covid 19"
        initView()
    }

    private fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        informationInCountryFragment =
            InformationInCountry.newInstance(TYPE_TAB_INFORMATION_COUNTRY)
        mFragments.add(informationInCountryFragment)

        informationInTheWorldFragment =
            InformationInTheWorld.newInstance(TYPE_TAB_INFORMATION_IN_THE_WORLD)
        mFragments.add(informationInTheWorldFragment)

        mVpRankingAdapter = CommonPagerAdapter(childFragmentManager, mFragments)
        covid_pager.apply {
            offscreenPageLimit = 4
            adapter = mVpRankingAdapter
//            currentItem = childPageOpen
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    val view = covid_pager.getChildAt(position)
                    val height = view.measuredHeight
                    val customHeight =
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                    val layoutParam = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        customHeight
                    )
                    covid_pager.layoutParams = layoutParam
                }
            })
        }
        setupTabLayout()
    }

    private fun setupTabLayout() {
        covid_tabs.setupWithViewPager(covid_pager)
        val countryTabTitle = resources.getString(R.string.tab_one)
        mInTheWorld = ItemTabBar(context, R.layout.item_tabs)
        mInTheWorld.setData(countryTabTitle, false)

        val inTheWorldTabTitle = resources.getString(R.string.tab_two)
        mCountry = ItemTabBar(context, R.layout.item_tabs)
        mCountry.setData(inTheWorldTabTitle, false)

        covid_tabs.getTabAt(0)?.customView = mInTheWorld
        covid_tabs.getTabAt(1)?.customView = mCountry
    }

    override fun observable() {
        // do nothing
    }

    override fun fireData() {
        // do nothing
    }
}