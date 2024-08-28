package com.oleg.ivanov.testpoint.di

import com.oleg.ivanov.testpoint.AppSettings
import com.oleg.ivanov.testpoint.network.NetworkService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {

    @ApplicationScope
    @Provides
    fun retrofit(
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(AppSettings.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @ApplicationScope
    @Provides
    fun okHttpClient(
        loggingBodyInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(loggingBodyInterceptor)
            .build()
    }

    @ApplicationScope
    @Provides
    fun loggingBodyInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @ApplicationScope
    @Provides
    fun networkService(retrofit: Retrofit): NetworkService =
        retrofit.create(NetworkService::class.java)

}