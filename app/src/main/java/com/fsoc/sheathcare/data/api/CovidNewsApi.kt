package com.fsoc.sheathcare.data.api

import com.fsoc.sheathcare.data.api.entity.CovidNewDto
import io.reactivex.Single
import retrofit2.http.GET

interface CovidNewsApi {
    @GET(ApiConfig.COVID_NEWS)
    fun getListCovidNews(): Single<CovidNewDto>
}