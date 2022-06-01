package com.fsoc.sheathcare.data.repository

import com.fsoc.sheathcare.data.api.CovidInTheWorldApi
import com.fsoc.sheathcare.data.api.CovidNewsApi
import com.fsoc.sheathcare.data.mapper.CovidInTheWorldDtoMapper
import com.fsoc.sheathcare.data.mapper.CovidNewsDtoMapper
import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModel
import com.fsoc.sheathcare.domain.entity.CovidNewsModel
import com.fsoc.sheathcare.domain.repository.CovidNewsRepo
import io.reactivex.Single
import javax.inject.Inject

class CovidNewsRepoImpl @Inject constructor() : CovidNewsRepo {
    @Inject
    lateinit var api: CovidNewsApi

    @Inject
    lateinit var apiInTheWorld: CovidInTheWorldApi

    @Inject
    lateinit var covidNewsDtoMapper: CovidNewsDtoMapper

    @Inject
    lateinit var covidInTheWorldDtoMapper: CovidInTheWorldDtoMapper
    override fun getListCovidNews(): Single<CovidNewsModel> {
        return api.getListCovidNews().map {
            return@map covidNewsDtoMapper.map(it)
        }
    }

    override fun getListCovidNewsInTheWorld(): Single<CovidInTheWorldModel> {
        return apiInTheWorld.getListCovidNewsInTheWorld()
            .map {
            return@map covidInTheWorldDtoMapper.map(it)
        }
    }


}