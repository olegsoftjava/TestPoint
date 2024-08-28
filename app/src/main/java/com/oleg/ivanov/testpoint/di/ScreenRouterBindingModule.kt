package com.oleg.ivanov.testpoint.di

import com.oleg.ivanov.testpoint.screen_router.ScreenRouter
import com.oleg.ivanov.testpoint.screen_router.ScreenRouterImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ScreenRouterBindingModule {

    @Binds
    @ApplicationScope
    abstract fun screenRouter(impl: ScreenRouterImpl): ScreenRouter

}