package com.fsoc.sheathcare.domain.repository

import com.fsoc.sheathcare.domain.entity.BaseModel
import io.reactivex.Single

interface BaseRepo {
    fun checkAppExpire(): Single<BaseModel>
    fun checkMaintenanceMode(): Single<BaseModel>
}