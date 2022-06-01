package com.fsoc.sheathcare.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fsoc.sheathcare.common.di.qualifier.ViewModelKey
import com.fsoc.sheathcare.presentation.base.ViewModelFactory
import com.fsoc.sheathcare.presentation.main.MainViewModel
import com.fsoc.sheathcare.presentation.main.auth.AuthViewModel
import com.fsoc.sheathcare.presentation.main.chat.ChatViewModel
import com.fsoc.sheathcare.presentation.main.covidnews.CovidNewsViewModel
import com.fsoc.sheathcare.presentation.main.home.SlideVideoViewModel
import com.fsoc.sheathcare.presentation.main.map.MapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CovidNewsViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: CovidNewsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    abstract fun bindMapsViewModel(viewModel: MapsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SlideVideoViewModel::class)
    abstract fun bindSlideVideoViewModel(viewModel: SlideVideoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel

}