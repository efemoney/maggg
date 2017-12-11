package com.efemoney.maggg.inject.module

import android.arch.lifecycle.ViewModelProvider
import com.efemoney.maggg.inject.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun factory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}