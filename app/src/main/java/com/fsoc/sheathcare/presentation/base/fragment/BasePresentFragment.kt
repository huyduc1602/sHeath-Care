//package com.fsoc.sheathcare.presentation.base.fragment
//
//import com.fsoc.sheathcare.common.extension.show
//import com.fsoc.sheathcare.presentation.base.BaseFragment
//import com.fsoc.sheathcare.presentation.base.BaseViewModel
//import kotlinx.android.synthetic.main.fragment_base.*
//
//abstract class BasePresentFragment<T : BaseViewModel> : BaseFragment<T>() {
//    override fun setUpView() {
//        mainActivity.showBottomNavMenu(false)
//    }
//
//    override fun setUpToolbarType() {
//        toolbarFragment.show(true)
//        toolbarFragmentSearch.show(false)
//        toolbarFragmentTitle.show(true)
//        actionInformation.show(false)
//        actionSetting.show(false)
//
//        toolbarFragmentIcon.show(false)
//        actionClose.show(true)
//
//        // set up listener
//        actionClose.click {
//            navigateBack()
//        }
//    }
//}
//
//
//abstract class BasePresentFragmentChild<T : BaseViewModel> : BaseFragment<T>() {
//    override fun setUpView() {
//        mainActivity.showBottomNavMenu(false)
//    }
//
//    override fun setUpToolbarType() {
//        toolbarFragment.show(true)
//        toolbarFragmentSearch.show(false)
//        toolbarFragmentTitle.show(true)
//        actionInformation.show(false)
//        actionSetting.show(false)
//
//        toolbarFragmentIcon.show(true)
//        actionClose.show(false)
//    }
//}