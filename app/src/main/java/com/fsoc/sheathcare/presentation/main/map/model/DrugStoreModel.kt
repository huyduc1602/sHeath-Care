package com.fsoc.sheathcare.presentation.main.map.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DrugStoreModel(
    val name: String,
    val latitude: Double,
    val longitude: Double,
//    val apartmentNumber: String,
    val city: String
//    val commentTotal: Int,
//    val country: String,
//    val district: String,
//    val evaluates: List<Evaluate>,
//    val id: Int,
//    val imageDrugStores: List<ImageDrugStore>,


//    val phoneNumber: String,
//    val products: List<Product>,
//    val province: String,
//    val services: List<Service>,
//    val status: Int,
//    val street: String,
//    val timeWorking: String,
//    val vote: Double
): Parcelable