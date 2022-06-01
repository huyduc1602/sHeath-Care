package com.fsoc.sheathcare.domain.usecase

import com.fsoc.sheathcare.domain.entity.*
import com.fsoc.sheathcare.domain.repository.SlideVideoRepo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SlideVideoUseCase @Inject constructor() {
    @Inject
    lateinit var slideVideoRepo: SlideVideoRepo
    fun getVideo(): Single<List<VideoShow>> {
        return slideVideoRepo.getVideo()
    }

    fun getProduct() : Single<List<String>>{
        return slideVideoRepo.getProduct()
    }

    fun getUser(): Single<User> {
        return slideVideoRepo.getUser()
    }

    fun getListProductCategory(): Single<List<ProductCategoryModel>> {
        return slideVideoRepo.getListProductCategory()
    }

    fun getInfoListProduct(path: String): Single<List<InfoProduct>> {
        return slideVideoRepo.getInfoListProduct(path)
    }

    fun getListProduct(): Single<List<InfoProduct>> {
        return slideVideoRepo.getListProduct()
    }

    fun getCommentProduct(path: String): Observable<MutableList<CommentProduct>> {
        return slideVideoRepo.getCommentProduct(path)
    }

    fun pushCommentProduct(
        path: String,
        id: String,
        content: String,
        avatar: String,
        seader: String,
        timestamp: FieldValue
    ): Single<CommentProduct> {
        return slideVideoRepo.pushCommentProduct(path, id, content, avatar, seader, timestamp)
    }

    // get list health news
    fun getListHealthNews(): Single<List<HealthNewsModel>> {
        return slideVideoRepo.getListHealthNews()
    }
}