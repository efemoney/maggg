package com.efemoney.maggg.inject.module

import com.efemoney.maggg.rx.ProductionRxSchedulers
import com.efemoney.maggg.rx.RxSchedulers
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RxSchedulerModule {

    @Binds
    @Singleton
    abstract fun rxSchedulers(value: ProductionRxSchedulers): RxSchedulers
}