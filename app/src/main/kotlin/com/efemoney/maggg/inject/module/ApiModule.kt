package com.efemoney.maggg.inject.module

import com.efemoney.maggg.BuildConfig
import com.efemoney.maggg.data.remote.TmdbApi
import com.efemoney.maggg.gson.TmdbImagePathAdapterFactory
import com.efemoney.maggg.inject.qualifier.ApiKey
import com.efemoney.maggg.interceptor.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun gson(): Gson {

        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapterFactory(TmdbImagePathAdapterFactory)
                .create()
    }

    @Provides
    @ApiKey
    fun apiKey() = BuildConfig.TMDB_API_KEY

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor()
                .setLevel(if (BuildConfig.DEBUG) BODY else NONE)
    }

    @Provides
    @Singleton
    fun okHttpClient(authInterceptor: AuthInterceptor, logger: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .addInterceptor(logger)
                .build()
    }

    @Provides
    @Singleton
    fun retrofit(client: OkHttpClient, gson: Gson): Retrofit {

        return Retrofit.Builder()
                .client(client)
                .baseUrl(TmdbApi.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun api(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)
}
