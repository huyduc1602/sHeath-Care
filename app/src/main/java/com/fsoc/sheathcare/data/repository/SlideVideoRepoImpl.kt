package com.fsoc.sheathcare.data.repository

import com.fsoc.sheathcare.domain.entity.*
import com.fsoc.sheathcare.domain.repository.SlideVideoRepo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SlideVideoRepoImpl @Inject constructor() : SlideVideoRepo {
    @Inject
    lateinit var fireStore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var listVideo: MutableList<VideoShow> = mutableListOf()
    private var listCategory: MutableList<ProductCategoryModel> = mutableListOf()
    private var listInfoProduct: MutableList<InfoProduct> = mutableListOf()
    private var listProduct : MutableList<String> = mutableListOf()
    private var listComment: MutableList<CommentProduct> = mutableListOf()
    private var listHealthNews: MutableList<HealthNewsModel> = mutableListOf()
    private var comment: CommentProduct = CommentProduct()
    override fun getVideo(): Single<List<VideoShow>> {
        return Single.create { emitter ->
            listVideo.clear()
            fireStore.collection("slides")
                .get().addOnSuccessListener { result ->
                    result.forEach {
                        listVideo.add(it.toObject(VideoShow::class.java))
                    }
                    emitter.onSuccess(listVideo)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }

    }

    override fun getUser(): Single<User> {
        val user = firebaseAuth.currentUser?.email.toString()
        return Single.create {
            fireStore.collection("users").document(user).get()
                .addOnSuccessListener { result ->
                    it.onSuccess(result.toObject(User::class.java)!!)
                }.addOnFailureListener { exception ->
                    it.onError(exception)
                }
        }
    }

    override fun getProduct(): Single<List<String>> {
        return Single.create { emitter ->
            listProduct.clear()
            fireStore.collection("product")
                .get()
                .addOnSuccessListener { result ->
                    result.forEach {
                        it.getString("name")?.let { it1 -> listProduct.add(it1) }
                    }
                    emitter.onSuccess(listProduct)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getListProductCategory(): Single<List<ProductCategoryModel>> {
        return Single.create { emitter ->
            listCategory.clear()
            fireStore.collection("productCategory")
                .get().addOnSuccessListener { result ->
                    result.forEach {
                        listCategory.add(it.toObject(ProductCategoryModel::class.java))
                    }
                    emitter.onSuccess(listCategory)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getInfoListProduct(path: String): Single<List<InfoProduct>> {
        return Single.create { emitter ->
            listInfoProduct.clear()
            fireStore.collection("product")
                .whereEqualTo("type", path)
                .get()
                .addOnSuccessListener { result ->
                    result.forEach {
                        listInfoProduct.add(it.toObject(InfoProduct::class.java))
                    }
                    emitter.onSuccess(listInfoProduct)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getListProduct(): Single<List<InfoProduct>> {
        return Single.create { emitter ->
            listInfoProduct.clear()
            fireStore.collection("product")
                .get()
                .addOnSuccessListener { result ->
                    result.forEach {
                        listInfoProduct.add(it.toObject(InfoProduct::class.java))
                    }
                    emitter.onSuccess(listInfoProduct)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getCommentProduct(path: String): Observable<MutableList<CommentProduct>> {
        return Observable.create { emitter ->
            fireStore.collection(path).orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->
                    listComment.clear()
                    value?.forEach {
                        listComment.add(it.toObject(CommentProduct::class.java))
                    }
                    emitter.onNext(listComment)
                    if (error != null) {
                        emitter.onError(error.fillInStackTrace())
                    }
                }


//                .addOnSuccessListener { result ->
//                    result.forEach {
//                        listComment.add(it.toObject(CommentProduct::class.java))
//                    }
//                    emitter.onSuccess(listComment)
//                }.addOnFailureListener { exception ->
//                    emitter.onError(exception)
//                }
        }
    }

    override fun pushCommentProduct(
        path: String,
        id: String,
        content: String,
        avatar: String,
        seader: String,
        timestamp: FieldValue
    ): Single<CommentProduct> {
        return Single.create { emitter ->
            val data = hashMapOf(
                "avatar" to avatar,
                "seader" to seader,
                "content" to content,
                "timestamp" to timestamp
            )
            fireStore.collection(path).document(id).set(data)
                .addOnSuccessListener { result ->
                    comment = CommentProduct(
                        avatar, seader, content, timestamp
                    )
                    emitter.onSuccess(comment)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getListHealthNews(): Single<List<HealthNewsModel>> {
        return Single.create { emitter ->
            listHealthNews.clear()
            fireStore.collection("healthNews")
                .get().addOnSuccessListener { result ->
                    result.forEach {
                        listHealthNews.add(it.toObject(HealthNewsModel::class.java))
                    }
                    emitter.onSuccess(listHealthNews)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

}
