package com.efemoney.maggg.rx

import io.reactivex.Scheduler

interface RxSchedulers {
    val computation: Scheduler
    val network: Scheduler
    val main: Scheduler
}