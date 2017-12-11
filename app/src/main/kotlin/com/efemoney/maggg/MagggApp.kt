package com.efemoney.maggg

import com.efemoney.maggg.inject.component.AppComponent
import com.efemoney.maggg.inject.component.DaggerAppComponent
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class MagggApp : DaggerApplication() {

    // Utilized by Glide
    @Inject lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector() = DaggerAppComponent.builder().create(this)
}