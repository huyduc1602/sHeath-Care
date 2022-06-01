package com.fsoc.sheathcare.common.di.module

import com.fsoc.sheathcare.data.api.entity.BaseDto
import com.fsoc.sheathcare.data.mapper.BaseDtoMapper
import com.fsoc.sheathcare.data.mapper.Mapper
import com.fsoc.sheathcare.domain.entity.BaseModel
import dagger.Binds
import dagger.Module

@Module
abstract class MapperModule {

    @Binds
    abstract fun provideBaseDto(baseDtoMapper: BaseDtoMapper): Mapper<BaseDto, BaseModel>
}