/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.common.coroutines

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking

fun <T : Any, R : Any> Flowable<T>.suspendMap(transform: suspend (T) -> R): Flowable<R> {
    return this
        .map { item -> runBlocking { transform(item) } }
        .subscribeOn(Schedulers.computation())
}

fun <T : Any, R : Any> Single<T>.suspendMap(transform: suspend (T) -> R): Single<R> {
    return this
        .map { item -> runBlocking { transform(item) } }
        .subscribeOn(Schedulers.computation())
}