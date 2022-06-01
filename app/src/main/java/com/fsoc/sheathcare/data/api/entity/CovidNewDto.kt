package com.fsoc.sheathcare.data.api.entity

data class CovidNewDto(
    val code: Int?,
    val `data`: CovidItemDto,
    val description: String?,
    val hash: Any?,
    val runtime: Double?,
    val status: Int?,
    val user: Any?
)

data class CovidItemDto(
    val global: GlobalDto,
    val vietnam: VietnamDto
)

data class GlobalDto(
    val confirmed: Int?,
    val deaths: Int?,
    val last: LastDto,
    val recovered: Int?,
    val update: Int?
)

data class VietnamDto(
    val confirmed: Int?,
    val deaths: Int?,
    val list: List<ProvinceDto>,
    val recovered: Int?,
    val update: Double?
)

data class LastDto(
    val confirmed: Int?,
    val deaths: Int?,
    val recovered: Int?
)

data class ProvinceDto(
    val confirmed: Int?,
    val deaths: Int?,
    val name: String?,
    val recovered: Int?,
    val update: Double?
)


