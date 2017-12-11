package com.efemoney.maggg.inject.component

import com.efemoney.maggg.MagggApp
import com.efemoney.maggg.glide.TmdbImageUrlConfig
import com.efemoney.maggg.inject.module.*
import com.efemoney.maggg.ui.detail.DetailInjector
import com.efemoney.maggg.ui.popular.PopularInjector
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ApiModule::class,
    GlideConfigModule::class,
    RepositoryModule::class,
    RxSchedulerModule::class,
    ViewModelFactoryModule::class,

    PopularInjector::class,
    DetailInjector::class
])
interface AppComponent : AndroidInjector<MagggApp> {

    @Named("forGlide")
    fun glideClient(): OkHttpClient

    fun imageUrlConfig(): TmdbImageUrlConfig

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MagggApp>() {

        abstract override fun build(): AppComponent
    }
}