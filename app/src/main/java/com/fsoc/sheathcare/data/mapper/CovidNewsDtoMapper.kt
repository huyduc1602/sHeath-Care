package com.fsoc.sheathcare.data.mapper

import com.fsoc.sheathcare.data.api.entity.*
import com.fsoc.sheathcare.domain.entity.*
import javax.inject.Inject

class CovidNewsDtoMapper @Inject constructor() : Mapper<CovidNewDto, CovidNewsModel>() {
    @Inject
    lateinit var itemMapper: CovidItemMapper
    override fun map(from: CovidNewDto): CovidNewsModel {
        return from.let {
            CovidNewsModel(
                it.code,
                itemMapper.map(it.data),
                it.description,
                it.hash,
                it.runtime,
                it.status,
                it.user
            )
        }
    }

    override fun reverse(to: CovidNewsModel): CovidNewDto {
        return to.let {
            CovidNewDto(
                it.code,
                itemMapper.reverse(it.data),
                it.description,
                it.hash,
                it.runtime,
                it.status,
                it.user
            )
        }
    }
}

class CovidItemMapper @Inject constructor() : Mapper<CovidItemDto, CovidItem>() {
    @Inject
    lateinit var itemGlobaMapper: GlobalMapper

    @Inject
    lateinit var itemVietNamMapperrr: VietNamMapper
    override fun map(from: CovidItemDto): CovidItem {
        return from.let {
            CovidItem(itemGlobaMapper.map(it.global), itemVietNamMapperrr.map(it.vietnam))
        }
    }

    override fun reverse(to: CovidItem): CovidItemDto {
        return to.let {
            CovidItemDto(
                itemGlobaMapper.reverse(it.global),
                itemVietNamMapperrr.reverse(it.vietnam)
            )
        }
    }
}

// map model Global model
class GlobalMapper @Inject constructor() : Mapper<GlobalDto, GlobalModel>() {
    @Inject
    lateinit var itemLastMapper: LastMapper
    override fun map(from: GlobalDto): GlobalModel {
        return from.let {
            GlobalModel(
                it.confirmed,
                it.deaths,
                itemLastMapper.map(it.last),
                it.recovered,
                it.update
            )
        }
    }

    override fun reverse(to: GlobalModel): GlobalDto {
        return to.let {
            GlobalDto(
                it.confirmed,
                it.deaths,
                itemLastMapper.reverse(it.last),
                it.recovered,
                it.update
            )
        }
    }
}

// map model vietnam model
class VietNamMapper @Inject constructor() : Mapper<VietnamDto, VietnamModel>() {
    @Inject
    lateinit var itemProvinceMapper: VietNamItemMapper
    override fun map(from: VietnamDto): VietnamModel {
        return from.let {
            VietnamModel(
                it.confirmed,
                it.deaths,
                itemProvinceMapper.map(it.list),
                it.recovered,
                it.update
            )
        }
    }

    override fun reverse(to: VietnamModel): VietnamDto {
        return to.let {
            VietnamDto(
                it.confirmed,
                it.deaths,
                itemProvinceMapper.reverse(it.listProvince),
                it.recovered,
                it.update
            )
        }
    }
}

class LastMapper @Inject constructor() : Mapper<LastDto, LastModel>() {
    override fun map(from: LastDto): LastModel {
        return from.let {
            LastModel(it.confirmed, it.deaths, it.recovered)
        }
    }

    override fun reverse(to: LastModel): LastDto {
        return to.let {
            LastDto(it.confirmed, it.deaths, it.recovered)
        }
    }
}

class VietNamItemMapper @Inject constructor() : Mapper<ProvinceDto, ProvinceModel>() {
    override fun map(from: ProvinceDto): ProvinceModel {
        return from.let {
            ProvinceModel(it.confirmed, it.deaths, it.name, it.recovered, it.update)
        }
    }

    override fun reverse(to: ProvinceModel): ProvinceDto {
        return to.let {
            ProvinceDto(it.confirmed, it.deaths, it.name, it.recovered, it.update)
        }
    }
}


// Mapper for List covid in the world

class CovidInTheWorldDtoMapper @Inject constructor() :
    Mapper<CovidInTheWorldDto, CovidInTheWorldModel>() {
    @Inject
    lateinit var itemListOfInfectedCaseInTheWorld: ItemListOfInfectedCaseInTheWorld
    override fun map(from: CovidInTheWorldDto): CovidInTheWorldModel {
        val to = CovidInTheWorldModel()
        from.forEach {
            to.add(itemListOfInfectedCaseInTheWorld.map(it))
        }
        return to
    }

    override fun reverse(to: CovidInTheWorldModel): CovidInTheWorldDto {
        val from = CovidInTheWorldDto()
        to.forEach {
            from.add(itemListOfInfectedCaseInTheWorld.reverse(it))
        }
        return from
    }
}

class ItemListOfInfectedCaseInTheWorld @Inject constructor() :
    Mapper<CovidInTheWorldItemDto, CovidInTheWorldModelItemModel>() {
    @Inject
    lateinit var itemCountryInfo: ItemCountryInfo
    override fun map(from: CovidInTheWorldItemDto): CovidInTheWorldModelItemModel {
        return from.let {
            CovidInTheWorldModelItemModel(
                it.active,
                it.activePerOneMillion,
                it.cases,
                it.casesPerOneMillion,
                it.continent,
                it.country,
                itemCountryInfo.map(it.countryInfo),
                it.critical,
                it.criticalPerOneMillion,
                it.deaths,
                it.deathsPerOneMillion,
                it.oneCasePerPeople,
                it.oneDeathPerPeople,
                it.oneTestPerPeople,
                it.population,
                it.recovered,
                it.recoveredPerOneMillion,
                it.tests,
                it.testsPerOneMillion,
                it.todayCases,
                it.todayDeaths,
                it.todayRecovered,
                it.undefined,
                it.updated
            )
        }
    }

    override fun reverse(to: CovidInTheWorldModelItemModel): CovidInTheWorldItemDto {
        return to.let {
            CovidInTheWorldItemDto(
                it.active,
                it.activePerOneMillion,
                it.cases,
                it.casesPerOneMillion,
                it.continent,
                it.country,
                itemCountryInfo.reverse(it.countryInfo),
                it.critical,
                it.criticalPerOneMillion,
                it.deaths,
                it.deathsPerOneMillion,
                it.oneCasePerPeople,
                it.oneDeathPerPeople,
                it.oneTestPerPeople,
                it.population,
                it.recovered,
                it.recoveredPerOneMillion,
                it.tests,
                it.testsPerOneMillion,
                it.todayCases,
                it.todayDeaths,
                it.todayRecovered,
                it.undefined,
                it.updated
            )
        }
    }
}

class ItemCountryInfo @Inject constructor() : Mapper<CountryInfoDto, CountryInfoModel>() {
    override fun map(from: CountryInfoDto): CountryInfoModel {
        return from.let {
            CountryInfoModel(it._id, it.flag, it.iso2, it.iso3, it.lat, it.long)
        }
    }

    override fun reverse(to: CountryInfoModel): CountryInfoDto {
        return to.let {
            CountryInfoDto(it._id, it.flag, it.iso2, it.iso3, it.lat, it.long)
        }
    }


}



