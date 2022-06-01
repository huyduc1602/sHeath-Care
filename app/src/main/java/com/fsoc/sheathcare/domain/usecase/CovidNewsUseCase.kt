package com.fsoc.sheathcare.domain.usecase

import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModel
import com.fsoc.sheathcare.domain.entity.CovidNewsModel
import com.fsoc.sheathcare.domain.repository.CovidNewsRepo
import io.reactivex.Single
import javax.inject.Inject

class CovidNewsUseCase @Inject constructor() {
    @Inject
    lateinit var repo: CovidNewsRepo
    fun getListCovidNews(): Single<CovidNewsModel> {
        return repo.getListCovidNews()
    }

    fun getListCovidNewsInTheWorld(): Single<CovidInTheWorldModel> {
        return repo.getListCovidNewsInTheWorld()
    }
}