/*
 * Created by IMStudio on 5/11/21 10:41 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:35 AM
 */

package com.nphau.android.shared

import com.nphau.android.shared.flags.OnLifecycleStart
import com.nphau.android.shared.flags.OnLifecycleStop
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

/**
 *
 * */
interface PluginInitializer {

    private val compositeDisposable: CompositeDisposable; get() = CompositeDisposable()

    /**
     * Initializes a module given the application Context
     */
    fun initialize(application: com.nphau.android.Launcher) {
        log(tag(), "Initializing ...")
    }

    @OnLifecycleStop
    fun onEnterBackground() {
        log(tag(), "onEnterBackground ...")
    }

    @OnLifecycleStart
    fun onEnterForeground() {
        log(tag(), "onEnterForeground ...")
    }

    fun addToDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun disposeAll() = compositeDisposable.clear()

    fun dependencies(): List<Class<out PluginInitializer>> = emptyList()

    fun Disposable.addToDisposable() = compositeDisposable.add(this)

    /**
     * A log function that use 3rd to log
     * @see <a href="https://github.com/JakeWharton/timber">Timber</a>
     */
    fun log(tag: String, message: String?) = Timber.tag(tag).d(message)

    fun tag(): String
}