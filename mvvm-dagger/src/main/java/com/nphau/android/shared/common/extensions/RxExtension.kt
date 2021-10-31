/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.common.extensions

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Safely dispose = if not null and not already disposed
 */
fun Disposable?.safeDispose() {
    if (this?.isDisposed == false) {
        dispose()
    }
}

// region [CompositeDisposable]
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)

// endregion

// region [Completable]
fun applyCompletableIoScheduler(): CompletableTransformer {
    return CompletableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun completable(block: (CompletableEmitter) -> Unit): Completable = Completable.create(block)

fun <T> completableFromCallable(block: () -> T): Completable = Completable.fromCallable(block)

@SuppressLint("CheckResult")
fun Completable.subscribeIgnoringResult() = subscribe({}, {})

// endregion

// region [Observable]
inline fun <reified T> Observable<T>.applyScheduler(scheduler: Scheduler): Observable<T> =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

inline fun <reified T> applyObservableIoScheduler(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable -> observable.applyScheduler(Schedulers.io()) }
}

fun <T> applyFormValidator(debounceTime: Long = 850): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .debounce(debounceTime, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> observable(block: (ObservableEmitter<T>) -> Unit): Observable<T> = Observable.create(block)

fun <T> observableFromCallable(block: () -> T): Observable<T> = Observable.fromCallable(block)

// endregion

// region [Flowable]
private fun <T> Flowable<T>.applyScheduler(scheduler: Scheduler): Flowable<T> =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

fun <T> applyFlowableIoScheduler(): FlowableTransformer<T, T> {
    return FlowableTransformer { flowable -> flowable.applyScheduler(Schedulers.io()) }
}

@SuppressLint("CheckResult")
fun <T> Flowable<T>.subscribeIgnoringResult() = subscribe({}, {})

// endregion

//region [Single]
inline fun <reified T> Single<T>.applyScheduler(scheduler: Scheduler): Single<T> =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

inline fun <reified T> applySingleIoScheduler(): SingleTransformer<T, T> {
    return SingleTransformer { single -> single.applyScheduler(Schedulers.io()) }
}

inline fun <reified T> Single<T>.runOnIoScheduler(): Single<T> = subscribeOn(Schedulers.io())

fun <T> single(block: (SingleEmitter<T>) -> Unit): Single<T> = Single.create(block)
fun <T> singleFromCallable(block: () -> T): Single<T> = Single.fromCallable(block)

@SuppressLint("CheckResult")
fun <T> Single<T>.subscribeIgnoringResult() = subscribe({}, {})

// endregion

// region [Maybe]
private fun <T> Maybe<T>.applyScheduler(scheduler: Scheduler): Maybe<T> =
    subscribeOn(scheduler).observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())

fun <T> applyMaybeIoScheduler(): MaybeTransformer<T, T> {
    return MaybeTransformer { mayBe -> mayBe.applyScheduler(Schedulers.io()) }
}

// endregion