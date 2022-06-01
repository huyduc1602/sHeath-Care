package com.fsoc.sheathcare.domain.usecase

import com.fsoc.sheathcare.domain.entity.BaseModel
import com.fsoc.sheathcare.domain.repository.BaseRepo
import io.reactivex.Single
import javax.inject.Inject

class BaseUseCase @Inject constructor() {
    @Inject
    lateinit var baseRepo: BaseRepo

    fun checkAppExpire(): Single<BaseModel> {
        return baseRepo.checkAppExpire()
    }

    fun checkMaintenanceMode(): Single<BaseModel> {
        return baseRepo.checkMaintenanceMode()
    }
}