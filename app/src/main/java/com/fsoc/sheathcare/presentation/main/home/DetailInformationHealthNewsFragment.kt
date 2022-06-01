package com.fsoc.sheathcare.presentation.main.home

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.fragment_detail_info_health_news.*

class DetailInformationHealthNewsFragment : BaseFragment<SlideVideoViewModel>() {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        const val HEALTH_NEWS_IMAGE = "HEALTH_NEWS_IMAGE"
        const val HEALTH_NEWS_PREVENTION = "HEALTH_NEWS_PREVENTION"
        const val HEALTH_NEWS_SYMPTOM = "HEALTH_NEWS_SYMPTOM"

    }

    private lateinit var healthNewsImage: String
    private lateinit var healthNewsPrevention: String
    override fun layoutRes(): Int {
        return R.layout.fragment_detail_info_health_news
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(activity ?: return, viewModelFactory)
            .get(SlideVideoViewModel::class.java)
    }


    override fun setUpView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Bản tin sức khỏe"
        initView()
        addBackPress(this) {
                findNavController().popBackStack()
        }
    }

    private fun initView() {
        healthNewsImage = arguments?.getString(HEALTH_NEWS_IMAGE) ?: ""
        healthNewsPrevention = arguments?.getString(HEALTH_NEWS_PREVENTION) ?: ""
        iv_info_health_news.loadImage(healthNewsImage)
        tv_prevention.text = healthNewsPrevention
        tv_symptom.text =  arguments?.getString(HEALTH_NEWS_SYMPTOM) ?: ""

    }

    override fun observable() {
        // do nothing
    }

    override fun fireData() {
        // do nothing
    }
}