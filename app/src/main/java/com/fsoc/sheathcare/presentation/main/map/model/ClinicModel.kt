package com.fsoc.sheathcare.presentation.main.map.model

import com.google.firebase.firestore.GeoPoint

data class ClinicModel(
    var address: String? = null,
    var city: String? = null,
    var imageCompany: String? = null,
    var location: GeoPoint? = null,
//    val apartmentNumber: String,
    var name: String? = null,
    var phoneNumber: String? = null,
    var type: Int? = null
//    val clinicWorkTimes: List<Any>,
//    val country: String,
//    val district: String,
//    val evaluates: List<Evaluate>,
//    val faculties: List<Faculty>,
//    val id: Int,
//    val imageClinics: List<ImageClinic>,
//    val information: String,
//    val languages: String,


//    val province: String,
//    val street: String,
//    val websiteUrl: String
)