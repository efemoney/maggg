package com.efemoney.maggg.inject.module

import com.efemoney.maggg.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import javax.inject.Named
import javax.inject.Singleton

@Module
class GlideConfigModule {

    @Provides
    @Singleton
    @Named("forGlide")
    fun loggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor()
                .setLevel(if (BuildConfig.DEBUG) BASIC else NONE)
    }

    @Provides
    @Singleton
    @Named("forGlide")
    fun okHttpClient(@Named("forGlide") logger: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
    }
}
