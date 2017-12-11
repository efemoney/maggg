package com.efemoney.maggg.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher

inline fun <T> LiveData<T>.observe(
        owner: LifecycleOwner,
        crossinline observer: (T?) -> Unit
) = this.observe(owner, Observer<T> { observer(it) })

fun <T: Any?> LiveData<T>.toPublisher(lo: LifecycleOwner) = LiveDataReactiveStreams.toPublisher<T>(lo, this)

fun <T: Any?> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)

fun <T: Any?> Observable<T>.toLiveData() = toFlowable(BackpressureStrategy.MISSING).toLiveData()

fun <T: Any?> Single<T>.toLiveData() = toFlowable().toLiveData()