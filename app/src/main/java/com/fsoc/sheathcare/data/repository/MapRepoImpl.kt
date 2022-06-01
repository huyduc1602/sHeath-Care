package com.fsoc.sheathcare.data.repository

import com.fsoc.sheathcare.domain.repository.MapRepo
import com.fsoc.sheathcare.presentation.main.map.model.ClinicModel
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import javax.inject.Inject

class MapRepoImpl @Inject constructor() : MapRepo {
    @Inject
    lateinit var fireStore: FirebaseFirestore
    private var listCompany = mutableListOf<ClinicModel>()
    private var listDrug = mutableListOf<ClinicModel>()
    override fun getCompany(type: Int?): Single<List<ClinicModel>> {
        return Single.create { emitter ->
            listCompany.clear()
//            if (type != null) {
            fireStore.collection("company")
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener { result ->
                    result.forEach {
                        listCompany.add(it.toObject(ClinicModel::class.java))
                    }
                    emitter.onSuccess(listCompany)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
//            } else {
//                fireStore.collection("company")
//                    .get()
//                    .addOnSuccessListener { result ->
//                        result.forEach {
//                            listCompany.add(it.toObject(ClinicModel::class.java))
//                        }
//                        emitter.onSuccess(listCompany)
//                    }.addOnFailureListener { exception ->
//                        emitter.onError(exception)
//                    }
//            }

        }
    }

    override fun getDrug(type: Int?): Single<List<ClinicModel>> {
        return Single.create { emitter ->
            listDrug.clear()
//            if (type != null) {
            fireStore.collection("company")
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener { result ->
                    result.forEach {
                        listDrug.add(it.toObject(ClinicModel::class.java))
                    }
                    emitter.onSuccess(listDrug)
                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }
}