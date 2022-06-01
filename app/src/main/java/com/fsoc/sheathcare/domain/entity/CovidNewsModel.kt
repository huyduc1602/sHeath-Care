package com.fsoc.sheathcare.domain.entity

data class CovidNewsModel(
    val code: Int?,
    val `data`: CovidItem,
    val description: String?,
    val hash: Any?,
    val runtime: Double?,
    val status: Int?,
    val user: Any?
)

data class CovidItem(
    val global: GlobalModel,
    val vietnam: VietnamModel
)

data class GlobalModel(
    val confirmed: Int?,
    val deaths: Int?,
    val last: LastModel,
    val recovered: Int?,
    val update: Int?
)

data class VietnamModel(
    val confirmed: Int?,
    val deaths: Int?,
    val listProvince: List<ProvinceModel>,
    val recovered: Int?,
    val update: Double?
)

data class LastModel(
    val confirmed: Int?,
    val deaths: Int?,
    val recovered: Int?
)

data class ProvinceModel(
    val confirmed: Int?,
    val deaths: Int?,
    val name: String?,
    val recovered: Int?,
    val update: Double?
)