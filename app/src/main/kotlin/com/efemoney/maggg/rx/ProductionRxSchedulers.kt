package com.efemoney.maggg.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductionRxSchedulers @Inject constructor() : RxSchedulers {
    override val computation: Scheduler = Schedulers.computation()
    override val network: Scheduler = Schedulers.io()
    override val main: Scheduler = AndroidSchedulers.mainThread()
}