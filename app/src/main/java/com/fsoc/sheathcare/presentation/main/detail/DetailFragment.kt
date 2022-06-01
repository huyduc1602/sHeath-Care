package com.fsoc.sheathcare.presentation.main.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.observe
import com.fsoc.sheathcare.common.extension.show
import com.fsoc.sheathcare.domain.entity.InfoProduct
import com.fsoc.sheathcare.domain.entity.ProductCategoryModel
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.MainActivity
import com.fsoc.sheathcare.presentation.main.detail.DetailInfoProductFragment.Companion.PATH
import com.fsoc.sheathcare.presentation.main.detail.adapter.PharmacyNewsStickyAdapter
import com.fsoc.sheathcare.presentation.main.detail.infordapter.SearchAdapter
import com.fsoc.sheathcare.presentation.main.home.SlideVideoViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : BaseFragment<SlideVideoViewModel>() {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var staffListModel: ProductCategoryModel? = null
    private val mData = arrayListOf<ProductCategoryModel>()
    var mProductCategoryModel = ProductCategoryModel()
    var listProduct = mutableListOf<String>()
    var listResultSearch = mutableListOf<String>()
    var listSearch = arrayListOf<InfoProduct>()
    var listSearchFirst = arrayListOf<InfoProduct>()
    lateinit var adapterSearch: SearchAdapter
    private val mAdapter: PharmacyNewsStickyAdapter by lazy {
        PharmacyNewsStickyAdapter(mData) { path ->
            val arg = Bundle().apply {
                putString(PATH, path)
            }
            navigate(R.id.infoProductFragment, arg)
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_detail
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SlideVideoViewModel::class.java)
    }

    override fun setUpView() {
        (activity as MainActivity).initView()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Sản phẩm"
        initList()
        eventsSearch()
    }

    override fun observable() {
        observe(viewModel.listProductCategory) {
            mData.clear()
            mData.addAll(it)
            mAdapter.notifyDataSetChanged()
//            updateList()
        }
        observe(viewModel.listProduct) { str ->
            listProduct.clear()
            listProduct.addAll(str)
        }
        observe(viewModel.listSearchProduct) { item ->
            listSearch.clear()
            listSearch.addAll(item)
        }
    }

    override fun fireData() {
        mProductCategoryModel.let {
            viewModel.getListProductCategory()
        }
        viewModel.getProduct()
        viewModel.getListSearch()
    }

    private fun eventsSearch() {
        editSearch.doAfterTextChanged { edtTable ->
            if (edtTable.toString().isNullOrBlank()) {
                listSearchFirst.clear()
                rvSearchProduct.show(false)
            } else {
                listSearchFirst.clear()
                rvSearchProduct.show(true)
                listSearch.forEach { item ->
                    if (item.name.toLowerCase().contains(edtTable.toString().toLowerCase())) {
                        listSearchFirst.add(item)
                    }
                }
                adapterSearch.updateData(listSearchFirst)
            }
        }
    }

    private fun initList() {
        rcv_list_category.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        rvSearchProduct.apply {
            adapterSearch = SearchAdapter(listSearchFirst) { product ->
                val arg = Bundle().apply {
                    putString(DetailInfoProductFragment.PRODUCT_IMAGE, product.image)
                    putString(DetailInfoProductFragment.PRODUCT_INGREDIENT, product.ingredient)
                    putFloat(
                        DetailInfoProductFragment.PRODUCT_LIKE_NUMBER,
                        product.likeNumber.toFloat()
                    )
                    putString(DetailInfoProductFragment.PRODUCT_NAME, product.name)
                    putString(DetailInfoProductFragment.PRODUCT_ORIGIN, product.origin)
                    putString(DetailInfoProductFragment.USER_MANUAL, product.userManual)
                    putString(DetailInfoProductFragment.PRODUCT_USES, product.uses)
                    putString(DetailInfoProductFragment.PRODUCT_SIDE_EFFECTS, product.sideEfects)
                    putString(DetailInfoProductFragment.ID_COMMENT, product.idComment)
                }
                navigatePresent(R.id.detailInforProductFragment, arg)
            }
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterSearch
        }
    }
}