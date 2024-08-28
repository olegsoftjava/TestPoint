package com.oleg.ivanov.testpoint.di

import com.oleg.ivanov.testpoint.provider.PointsDataProvider
import com.oleg.ivanov.testpoint.provider.PointsDataProviderImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ProviderBindingModule {

    @Binds
    @ApplicationScope
    abstract fun pointsDataProvider(impl: PointsDataProviderImpl): PointsDataProvider

}