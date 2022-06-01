package com.fsoc.sheathcare.data.api.entity

class CovidInTheWorldDto : ArrayList<CovidInTheWorldItemDto>()

data class CovidInTheWorldItemDto(
    val active: Int?,
    val activePerOneMillion: Double?,
    val cases: Int?,
    val casesPerOneMillion: Int?,
    val continent: String?,
    val country: String?,
    val countryInfo: CountryInfoDto,
    val critical: Int?,
    val criticalPerOneMillion: Double?,
    val deaths: Int?,
    val deathsPerOneMillion: Float?,
    val oneCasePerPeople: Int?,
    val oneDeathPerPeople: Int?,
    val oneTestPerPeople: Int?,
    val population: Int?,
    val recovered: Int?,
    val recoveredPerOneMillion: Double?,
    val tests: Int?,
    val testsPerOneMillion: Int?,
    val todayCases: Int?,
    val todayDeaths: Int?,
    val todayRecovered: Int?,
    val undefined: Float?,
    val updated: Long?
)

data class CountryInfoDto(
    val _id: Int?,
    val flag: String?,
    val iso2: String?,
    val iso3: String?,
    val lat: Float?,
    val long: Float?
)