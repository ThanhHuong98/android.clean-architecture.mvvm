/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.functional

import android.view.LayoutInflater
import android.view.ViewGroup

typealias unit<T> = (T) -> Unit

// CalBack
typealias CallBack = () -> Unit

fun CallBack.call() = this()
fun <T> consume(result: T, block: CallBack): T {
    block.call()
    return result
}
// Transformer
typealias Transformer<I, O> = (I) -> O

typealias Condition = () -> Boolean
typealias Predicate<T> = (T) -> Boolean

// Provider
typealias Provider<T> = () -> T

fun <R> Provider<R>.get() = this()

// Do not misue no point in creating unnecessary null pointers
fun <T> tryOrNull(provider: Provider<T>) = try {
    provider.get()
} catch (e: Throwable) {
    e.printStackTrace()
    null
}

typealias BindingInflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB