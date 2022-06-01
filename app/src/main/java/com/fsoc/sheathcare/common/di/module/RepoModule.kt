package com.fsoc.sheathcare.common.di.module

import com.fsoc.sheathcare.data.repository.*
import com.fsoc.sheathcare.domain.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule() {

    @Provides
    @Singleton
    fun provideBaseRepo(repoImpl: BaseRepoImpl): BaseRepo {
        return repoImpl
    }

    @Provides
    @Singleton
    fun provideAuthRepo(repoImpl: AuthRepoImpl): AuthRepo {
        return repoImpl
    }

    @Provides
    @Singleton
    fun provideCovidRepo(repoImpl: CovidNewsRepoImpl): CovidNewsRepo {
        return repoImpl
    }

    @Provides
    @Singleton
    fun provideSlideVideoRepo(repoImpl: SlideVideoRepoImpl): SlideVideoRepo {
        return repoImpl
    }

    @Provides
    @Singleton
    fun provideChatRepo(repoImpl: ChatRepoImpl): ChatRepo {
        return repoImpl
    }

    @Provides
    @Singleton
    fun provideMapRepo(repoImpl: MapRepoImpl): MapRepo {
        return repoImpl
    }
}
