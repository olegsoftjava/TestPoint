package com.oleg.ivanov.testpoint.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val contextApp: Context) {

    @ApplicationScope
    @Provides
    fun getAppContext() = contextApp

}