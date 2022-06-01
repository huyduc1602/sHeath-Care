package com.fsoc.sheathcare.presentation.main.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.domain.entity.InfoProduct
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.detail.DetailInfoProductFragment.Companion.PATH
import com.fsoc.sheathcare.presentation.main.detail.infordapter.InfoProductAdapter
import com.fsoc.sheathcare.presentation.main.home.SlideVideoViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_info_product.*

class InfoProductFragment : BaseFragment<SlideVideoViewModel>() {
    private lateinit var pathType : String
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private val mData = arrayListOf<InfoProduct>()
    var mInfoProduct = InfoProduct()

    private val mAdapter: InfoProductAdapter by lazy {
        InfoProductAdapter(mData) {
            clickItemMovie(it)
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_info_product
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SlideVideoViewModel::class.java)
    }

    override fun setUpView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Danh sách sản phẩm"

//        (activity as MainActivity).actionBar.apply {
//
//        }
        pathType = arguments?.getString(PATH).toString()
        initList()
    }

    private fun initList() {
        rcv_info_product.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            adapter = mAdapter
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
        observe(viewModel.listInfoProduct) {
//            showLoading(false)
            mData.clear()
            mData.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun fireData() {
        mInfoProduct.let {
            viewModel.getListInfoProduct(pathType)
        }
    }

    private fun clickItemMovie(obj: InfoProduct) {
        val arg = Bundle().apply {
            putString(DetailInfoProductFragment.PRODUCT_IMAGE, obj.image)
            putString(DetailInfoProductFragment.PRODUCT_INGREDIENT, obj.ingredient)
            putFloat(DetailInfoProductFragment.PRODUCT_LIKE_NUMBER, obj.likeNumber.toFloat())
            putString(DetailInfoProductFragment.PRODUCT_NAME, obj.name)
            putString(DetailInfoProductFragment.PRODUCT_ORIGIN, obj.origin)
            putString(DetailInfoProductFragment.USER_MANUAL, obj.userManual)
            putString(DetailInfoProductFragment.PRODUCT_USES, obj.uses)
            putString(DetailInfoProductFragment.PRODUCT_SIDE_EFFECTS, obj.sideEfects)
            putString(DetailInfoProductFragment.ID_COMMENT,obj.idComment)

        }
        navigatePresent(R.id.detailInforProductFragment, arg)
    }
}