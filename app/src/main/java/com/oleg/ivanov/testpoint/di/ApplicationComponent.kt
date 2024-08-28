package com.oleg.ivanov.testpoint.di

import com.oleg.ivanov.testpoint.presentation.points_screen.MainActivity
import com.oleg.ivanov.testpoint.presentation.table_screen.TableActivity
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class ApplicationScope

@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        ProviderBindingModule::class,
        RepositoryBindingModule::class,
        RetrofitModule::class,
        ScreenRouterBindingModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(tableActivity: TableActivity)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): ApplicationComponent
    }
}
