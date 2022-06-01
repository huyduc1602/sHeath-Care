package com.fsoc.sheathcare.domain.repository

import com.fsoc.sheathcare.domain.entity.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface SlideVideoRepo {
    fun getVideo() : Single<List<VideoShow>>
    fun getUser() : Single<User>
    fun getProduct() : Single<List<String>>
    fun getListProductCategory(): Single<List<ProductCategoryModel>>
    fun getInfoListProduct(path : String): Single<List<InfoProduct>>
    fun getListProduct(): Single<List<InfoProduct>>
    fun getListHealthNews(): Single<List<HealthNewsModel>>
    fun getCommentProduct(path: String) : Observable<MutableList<CommentProduct>>
    fun pushCommentProduct(path: String,id : String, content : String, avatar : String, seader : String, timestamp: FieldValue) : Single<CommentProduct>
}