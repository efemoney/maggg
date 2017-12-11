package com.efemoney.maggg.inject.module

import com.efemoney.maggg.data.Repository
import com.efemoney.maggg.data.TmdbRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun repository(value: TmdbRepository): Repository
}