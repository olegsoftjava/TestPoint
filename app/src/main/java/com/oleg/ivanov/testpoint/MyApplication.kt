package com.oleg.ivanov.testpoint

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import com.oleg.ivanov.testpoint.di.AppModule
import com.oleg.ivanov.testpoint.di.ApplicationComponent
import com.oleg.ivanov.testpoint.di.DaggerApplicationComponent

class MyApplication : Application(), DefaultLifecycleObserver {

    companion object {
        lateinit var appComponent: ApplicationComponent
        lateinit var instance: MyApplication
            private set
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super<Application>.onCreate()

        initDaggerComponent()

    }

    private fun initDaggerComponent() {
        appComponent = DaggerApplicationComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

}