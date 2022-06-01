package com.fsoc.sheathcare.presentation.main.covidnews

import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.applyIoScheduler
import com.fsoc.sheathcare.domain.entity.CovidInTheWorldModel
import com.fsoc.sheathcare.domain.entity.CovidNewsModel
import com.fsoc.sheathcare.domain.entity.User
import com.fsoc.sheathcare.domain.usecase.CovidNewsUseCase
import com.fsoc.sheathcare.presentation.main.MainViewModel
import javax.inject.Inject

class CovidNewsViewModel @Inject constructor() : MainViewModel() {
    @Inject
    lateinit var useCase: CovidNewsUseCase
    val covidList = MutableLiveData<CovidNewsModel>()
    val covidListInTheWorld = MutableLiveData<CovidInTheWorldModel>()

    fun getListCovidNews() {
        useCase.getListCovidNews().applyIoScheduler(mLoading).subscribe({ model ->
            covidList.value = model
        }, { error ->
            mError.value = error
        }).track()
    }

    fun getListCovidNewsInTheWorld() {
        useCase.getListCovidNewsInTheWorld().applyIoScheduler(mLoading).subscribe({ model ->
            covidListInTheWorld.value = model
        }, { error ->
             mError.value = error
        }).track()
    }

}