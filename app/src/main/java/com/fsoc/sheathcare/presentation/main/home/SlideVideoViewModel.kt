package com.fsoc.sheathcare.presentation.main.home

import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.applyIoScheduler
import com.fsoc.sheathcare.domain.entity.*
import com.fsoc.sheathcare.domain.usecase.SlideVideoUseCase
import com.fsoc.sheathcare.presentation.main.MainViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class SlideVideoViewModel @Inject constructor() : MainViewModel() {
    @Inject
    lateinit var slideVideoUseCase: SlideVideoUseCase

    val userProfile = MutableLiveData<User>()
    var listVideo = MutableLiveData<List<VideoShow>>()
    var listProductCategory = MutableLiveData<List<ProductCategoryModel>>()
    var listHealthNews = MutableLiveData<List<HealthNewsModel>>()
    var listInfoProduct = MutableLiveData<List<InfoProduct>>()
    var listComment = MutableLiveData<MutableList<CommentProduct>>()
    var listProduct = MutableLiveData<List<String>>()
    var listSearchProduct = MutableLiveData<List<InfoProduct>>()
    var comment = MutableLiveData<CommentProduct>()
    fun getData() {
        slideVideoUseCase.getVideo()
            .applyIoScheduler(mLoading)
            .subscribe({ data ->
                listVideo.value = data
            }, { error -> mError.value = error }).track()
    }

    fun getProduct(){
        slideVideoUseCase.getProduct()
            .applyIoScheduler(mLoading)
            .subscribe({ str ->
                listProduct.value = str
            }, { error -> mError.value = error }).track()
    }

    fun getListProductCategory() {
        slideVideoUseCase.getListProductCategory().applyIoScheduler(mLoading)
            .subscribe({ data ->
                listProductCategory.value = data
            }, { error ->
                mError.value = error
            }).track()
    }

    fun getListInfoProduct(path: String) {
        slideVideoUseCase.getInfoListProduct(path).applyIoScheduler(mLoading)
            .subscribe({ data ->
                listInfoProduct.value = data
            }, { error ->
                mError.value = error
            }).track()
    }

    fun getListSearch() {
        slideVideoUseCase.getListProduct().applyIoScheduler(mLoading)
            .subscribe({ data ->
                listSearchProduct.value = data
            }, { error ->
                mError.value = error
            }).track()
    }

    fun getUser() {
        slideVideoUseCase.getUser()
            .applyIoScheduler(mLoading)
            .subscribe({ user ->
                userProfile.value = user
            }, { error -> mError.value = error }).track()
    }

    fun getCommentProduct(path: String) {
        slideVideoUseCase.getCommentProduct(path)
            .applyIoScheduler(mLoading)
            .subscribe({ comment ->
                listComment.value = comment
            }, { error -> mError.value = error }).track()
    }

    fun pushCommentProduct(
        path: String,
        id : String,
        content: String,
        avatar: String,
        seader: String,
        timestamp: FieldValue
    ) {
        slideVideoUseCase.pushCommentProduct(path,id, content, avatar, seader,timestamp)
            .subscribe({ comment ->
                this.comment.value = comment
            }, { error -> mError.value = error }).track()
    }

    // get Health News
    fun getListHealthNews() {
        slideVideoUseCase.getListHealthNews().applyIoScheduler(mLoading)
            .subscribe({ data ->
                listHealthNews.value = data
            }, { error ->
                mError.value = error
            }).track()
    }
}