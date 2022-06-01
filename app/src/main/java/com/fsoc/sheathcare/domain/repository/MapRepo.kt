package com.fsoc.sheathcare.domain.repository

import com.fsoc.sheathcare.presentation.main.map.model.ClinicModel
import io.reactivex.Single

interface MapRepo {
    fun getCompany(type: Int? = null): Single<List<ClinicModel>>
    fun getDrug(type: Int? = null): Single<List<ClinicModel>>

}