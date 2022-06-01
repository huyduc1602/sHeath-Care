package com.fsoc.sheathcare.presentation.main.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.fsoc.sheathcare.common.extension.applyIoScheduler
import com.fsoc.sheathcare.domain.usecase.MapUseCase
import com.fsoc.sheathcare.presentation.main.MainViewModel
import com.fsoc.sheathcare.presentation.main.map.model.ClinicModel
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class MapsViewModel @Inject constructor() : MainViewModel() {
    @Inject
    lateinit var mapUseCase: MapUseCase
    var listCompany = MutableLiveData<List<ClinicModel>>()
    var listDrugStore = MutableLiveData<List<ClinicModel>>()

    private var listCityName: MutableList<Address> = mutableListOf()
    fun getCityName(context: Context, latitude: Double, longitude: Double): String {
        val gcd = Geocoder(context, Locale.getDefault())
        listCityName = gcd.getFromLocation(latitude, longitude, 10)
        if (listCityName.size > 0) {
            var cityName = listCityName[0].adminArea
            return cityName
        } else {
            return ""
        }
    }

    fun getCompany(type: Int? = null) {
        mapUseCase.getCompany(type)
            .applyIoScheduler(mLoading)
            .subscribe({ data ->
                listCompany.value = data
            }, { error ->
                mError.value = error
            }).track()
    }

    fun getDrugStore(type: Int? = null) {
        mapUseCase.getDrug(type)
            .applyIoScheduler(mLoading)
            .subscribe({ data ->
                listDrugStore.value = data
            }, { error ->
                mError.value = error
            }).track()
    }
}

