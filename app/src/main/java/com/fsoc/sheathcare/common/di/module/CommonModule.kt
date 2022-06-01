package com.fsoc.sheathcare.common.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return context
    }
}
