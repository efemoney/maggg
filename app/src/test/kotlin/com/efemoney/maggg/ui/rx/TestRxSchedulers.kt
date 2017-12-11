package com.efemoney.maggg.ui.rx

import com.efemoney.maggg.rx.RxSchedulers
import io.reactivex.schedulers.Schedulers

class TestRxSchedulers: RxSchedulers {
    override val computation = Schedulers.trampoline()
    override val network = Schedulers.trampoline()
    override val main = Schedulers.trampoline()
}