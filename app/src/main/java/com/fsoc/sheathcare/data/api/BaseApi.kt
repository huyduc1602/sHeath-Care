package com.fsoc.sheathcare.data.api

import com.fsoc.sheathcare.data.api.entity.BaseDto
import io.reactivex.Single
import retrofit2.http.GET

interface BaseApi {
    @GET(ApiConfig.CHECK_APP_EXPIRE)
    fun getCheckAppExpire(): Single<BaseDto>

    @GET(ApiConfig.CHECK_MAINTENANCE_MODE)
    fun checkMaintenanceMode(): Single<BaseDto>
}