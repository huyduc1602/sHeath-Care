package com.fsoc.sheathcare.domain.usecase

import com.fsoc.sheathcare.domain.repository.MapRepo
import com.fsoc.sheathcare.presentation.main.map.model.ClinicModel
import io.reactivex.Single
import javax.inject.Inject

class MapUseCase @Inject constructor() {
    @Inject
    lateinit var mapRepo: MapRepo
    fun getCompany(type : Int? = null): Single<List<ClinicModel>> {
        return mapRepo.getCompany(type)
    }

    fun getDrug(type : Int? = null): Single<List<ClinicModel>> {
        return mapRepo.getDrug(type)
    }
}