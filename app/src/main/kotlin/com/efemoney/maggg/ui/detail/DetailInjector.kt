package com.efemoney.maggg.ui.detail

import android.arch.lifecycle.ViewModel
import com.efemoney.maggg.inject.ViewModelClassKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class DetailInjector {

    @ContributesAndroidInjector(modules = [DetailModule::class])
    abstract fun injector(): DetailActivity

    @Binds
    @IntoMap
    @ViewModelClassKey(DetailViewModel::class)
    internal abstract fun viewModel(viewModel: DetailViewModel): ViewModel
}