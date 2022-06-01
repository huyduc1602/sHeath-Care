package com.fsoc.sheathcare.common.di

import com.fsoc.sheathcare.common.di.module.*
import com.fsoc.sheathcare.presentation.main.auth.LoginFragment
import com.fsoc.sheathcare.presentation.main.auth.RegisterFragment
import com.fsoc.sheathcare.presentation.main.chat.ChatDetailFragment
import com.fsoc.sheathcare.presentation.main.chat.ChatFragment
import com.fsoc.sheathcare.presentation.main.covidnews.CovidNewsFragment
import com.fsoc.sheathcare.presentation.main.covidnews.InformationInCountry
import com.fsoc.sheathcare.presentation.main.covidnews.InformationInTheWorld
import com.fsoc.sheathcare.presentation.main.detail.*
import com.fsoc.sheathcare.presentation.main.home.DetailInformationHealthNewsFragment
import com.fsoc.sheathcare.presentation.main.home.HomeFragment
import com.fsoc.sheathcare.presentation.main.home.HealthNewsFragment
import com.fsoc.sheathcare.presentation.main.home.HomeAndHealthNewsTabFragment
import com.fsoc.sheathcare.presentation.main.map.MapsFragment
import com.fsoc.sheathcare.presentation.main.setting.SettingFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DaoModule::class, CommonModule::class, MapperModule::class, RepoModule::class, ViewModelModule::class, FirebaseModule::class])
interface AppComponent {
    // inject fragment
    fun inject(fragment: HomeFragment)
    fun inject(fragment: MapsFragment)
    fun inject(fragment: SettingFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: CovidNewsFragment)
    fun inject(fragment: ChatFragment)
    fun inject(fragment: InformationInCountry)
    fun inject(fragment: InformationInTheWorld)
    fun inject(fragment: DetailInfoProductFragment)
    fun inject(fragment: InfoProductFragment)
    fun inject(fragment: CommentProductFragment)
    fun inject(fragmentHealth: HomeAndHealthNewsTabFragment)
    fun inject(fragmentHealth: HealthNewsFragment)
    fun inject(fragmentHealth: DetailInformationHealthNewsFragment)
    fun inject(fragment: ChatDetailFragment)


}