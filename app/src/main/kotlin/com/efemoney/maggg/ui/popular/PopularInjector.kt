package com.efemoney.maggg.ui.popular

import android.arch.lifecycle.ViewModel
import com.efemoney.maggg.inject.ViewModelClassKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PopularInjector {

    @ContributesAndroidInjector(modules = [PopularModule::class])
    internal abstract fun injector(): PopularActivity

    @Binds
    @IntoMap
    @ViewModelClassKey(PopularViewModel::class)
    internal abstract fun viewModel(viewModel: PopularViewModel): ViewModel
}