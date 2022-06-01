package com.fsoc.sheathcare.data.api

import com.fsoc.sheathcare.data.api.entity.CovidInTheWorldDto
import io.reactivex.Single
import retrofit2.http.GET

interface CovidInTheWorldApi {

    @GET(ApiConfig.COVID_NEWS_IN_THE_WORLD)
    fun getListCovidNewsInTheWorld(): Single<CovidInTheWorldDto>
}