package com.oleg.ivanov.testpoint.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oleg.ivanov.testpoint.presentation.points_screen.view_model.PointsViewModelImpl
import com.oleg.ivanov.testpoint.presentation.table_screen.view_model.TableViewModelImpl
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PointsViewModelImpl::class)
    abstract fun bindPointsViewModel(viewModel: PointsViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TableViewModelImpl::class)
    abstract fun bindTableViewModel(viewModel: TableViewModelImpl): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
