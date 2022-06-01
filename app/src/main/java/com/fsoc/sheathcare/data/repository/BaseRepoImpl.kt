package com.fsoc.sheathcare.data.repository

import com.fsoc.sheathcare.data.api.BaseApi
import com.fsoc.sheathcare.data.api.entity.BaseDto
import com.fsoc.sheathcare.data.mapper.Mapper
import com.fsoc.sheathcare.domain.entity.BaseModel
import com.fsoc.sheathcare.domain.repository.BaseRepo
import io.reactivex.Single
import javax.inject.Inject

class BaseRepoImpl @Inject constructor() : BaseRepo {
    @Inject
    lateinit var baseApi: BaseApi
    @Inject
    lateinit var mapperBaseDto: Mapper<BaseDto, BaseModel>

    override fun checkAppExpire(): Single<BaseModel> {
        return baseApi.getCheckAppExpire().map {
            return@map mapperBaseDto.map(it)
        }
    }

    override fun checkMaintenanceMode(): Single<BaseModel> {
        return baseApi.checkMaintenanceMode().map {
            return@map mapperBaseDto.map(it)
        }
    }
}