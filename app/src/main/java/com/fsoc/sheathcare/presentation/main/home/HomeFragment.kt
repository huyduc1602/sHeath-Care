package com.fsoc.sheathcare.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.VideoShow
import com.fsoc.sheathcare.presentation.adapter.VideoAdapter
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<SlideVideoViewModel>() {
    private var listVideo: ArrayList<VideoShow> = arrayListOf()
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var mScrollListener: OnScrollListener
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(typeTab: Int): HomeFragment {
            val ret = HomeFragment()
            val b = Bundle()
            ret.arguments = b
            ret.typeTab = typeTab
            return ret
        }
    }

    var typeTab = 0

    override fun layoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(activity ?: return, viewModelFactory)
            .get(SlideVideoViewModel::class.java)
    }

    override fun onDestroy() {
        mScrollListener.releasePlayer()
        super.onDestroy()
    }

    override fun onDestroyView() {
        mScrollListener.releasePlayer()
        super.onDestroyView()
    }


    override fun setUpView() {
//        initData()
//        (activity as MainActivity).initView()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Thông tin sức khỏe"
//        toolbarFragment.show(false)
        val layoutManager = LinearLayoutManager(requireContext())
        videoAdapter = VideoAdapter(listVideo, initGlide(), requireContext())
        rvVideoHealthy.layoutManager = layoutManager
        rvVideoHealthy.adapter = videoAdapter
        mScrollListener = OnScrollListener(requireContext())
        rvVideoHealthy.addOnScrollListener(mScrollListener)
        setOnItemAttachListener()
    }

    private fun initGlide(): RequestManager {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

    private fun setOnItemAttachListener() {
        rvVideoHealthy.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                mScrollListener.resetVideoView(view)
            }

            override fun onChildViewAttachedToWindow(view: View) {}
        })
    }

    override fun observable() {
        observe(viewModel.listVideo) {
            videoAdapter.setData(it)
        }

        observe(viewModel.userProfile) {
            MainActivity.user.apply {
                email = it.email
                name = it.name
                token = it.token
                age = it.age
                type = it.type
                avatar = it.avatar
            }
        }
    }

    override fun fireData() {
        viewModel.getData()
        viewModel.getUser()
    }
}