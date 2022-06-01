package com.fsoc.sheathcare.domain.repository

import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModel
import com.fsoc.sheathcare.domain.entity.CovidNewsModel
import io.reactivex.Single

interface CovidNewsRepo {
    fun getListCovidNews(): Single<CovidNewsModel>
    fun getListCovidNewsInTheWorld(): Single<CovidInTheWorldModel>
}