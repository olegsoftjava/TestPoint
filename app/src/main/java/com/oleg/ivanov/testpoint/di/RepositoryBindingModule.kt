package com.oleg.ivanov.testpoint.di

import com.oleg.ivanov.testpoint.repository.PointsManager
import com.oleg.ivanov.testpoint.repository.PointsManagerImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryBindingModule {

    @Binds
    @ApplicationScope
    abstract fun pointsManager(impl: PointsManagerImpl): PointsManager

}