package com.fsoc.sheathcare.presentation.main.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fsoc.sheathcare.R
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.extension.click
import com.fsoc.sheathcare.presentation.base.BaseFragment
import com.fsoc.sheathcare.presentation.main.home.SlideVideoViewModel
import com.ominext.healthy.common.extension.loadImage
import kotlinx.android.synthetic.main.fragment_detail_info_product.*

class DetailInfoProductFragment : BaseFragment<SlideVideoViewModel>() {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    companion object {
        const val PATH = "PATH"
        const val PRODUCT_IMAGE = "PRODUCT_IMAGE"
        const val PRODUCT_INGREDIENT = "PRODUCT_INGREDIENT"
        const val PRODUCT_LIKE_NUMBER = "PRODUCT_LIKE_NUMBER"
        const val PRODUCT_NAME = "PRODUCT_NAME"
        const val PRODUCT_ORIGIN = "PRODUCT_ORIGIN"
        const val USER_MANUAL = "USER_MANUAL"
        const val PRODUCT_USES = "PRODUCT_USES"
        const val PRODUCT_SIDE_EFFECTS = "PRODUCT_SIDE_EFFECTS"
        const val ID_COMMENT = "ID_COMMENT"

    }

    private lateinit var productionImage: String
    private lateinit var productIngredient: String
    private var rating: Float = 0.0f
    private lateinit var productName: String
    private lateinit var productOrigin: String
    private lateinit var productUserManual: String
    private lateinit var productUses: String
    private lateinit var productSideEffect: String
    private lateinit var idComment: String
    override fun layoutRes(): Int {
        return R.layout.fragment_detail_info_product
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SlideVideoViewModel::class.java)
    }

    override fun setUpView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Chi tiết sản phẩm"
        initData()
        addEvents()
    }

    private fun addEvents() {
        tv_evaluate.click {
            val arg = Bundle().apply {
                putString(ID_COMMENT, idComment)
            }
            navigate(R.id.commentProductFragment, arg)
        }
    }

    private fun initData() {
        idComment = arguments?.getString(ID_COMMENT) ?: ""
        productionImage = arguments?.getString(PRODUCT_IMAGE) ?: ""
        productIngredient = arguments?.getString(PRODUCT_INGREDIENT) ?: ""
        rating = arguments?.getFloat(PRODUCT_LIKE_NUMBER) ?: 0f
        productName = arguments?.getString(PRODUCT_NAME) ?: ""
        productOrigin = arguments?.getString(PRODUCT_ORIGIN) ?: ""
        productUserManual = arguments?.getString(USER_MANUAL) ?: ""
        productUses = arguments?.getString(PRODUCT_USES) ?: ""
        productSideEffect = arguments?.getString(PRODUCT_SIDE_EFFECTS) ?: ""

        iv_detail_product.loadImage(productionImage)
        tv_name_product.text = productName
        rating_bar_info_product.rating = rating
        tv_uses.text = productUses
        tv_ingredient.text = productIngredient
        tv_userManual.text = productUserManual
        tv_sideEffects.text = productSideEffect
        date_of_manufacture.text = productOrigin
    }

    override fun observable() {
        // do nothing
    }

    override fun fireData() {
        // do nothing
    }
}