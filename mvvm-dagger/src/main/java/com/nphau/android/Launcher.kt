/*
 * Created by nphau on 31/10/2021, 21:42
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:40
 */

package com.nphau.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.nphau.android.shared.Environment
import com.nphau.android.shared.Plugins
import com.nphau.android.shared.libs.LocaleUtils
import com.nphau.android.shared.libs.NetworkUtils
import com.nphau.android.shared.libs.logger.DebugLoggingTree
import com.nphau.android.shared.libs.logger.ReleaseLoggingTree
import com.uber.rxdogtag.RxDogTag
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

abstract class Launcher : DaggerApplication(), LifecycleObserver {

    @Inject
    lateinit var initializers: Plugins

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()

        initializers.initWith(this)

        RxDogTag.install()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Language setting
        LocaleUtils.initializeWithDefaults(this)

        if (Environment.isDebug()) {
            Timber.plant(DebugLoggingTree())
            Stetho.initializeWithDefaults(this)
            // Fixing formatting
            RxJavaPlugins.setErrorHandler { throwable ->
                throwable.stackTraceToString()
                    .split("\n")
                    .forEach { Log.e("RxJava", it) }
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), throwable)
            }
        } else {
            Timber.plant(releaseLoggingTree())
        }
        NetworkUtils.initializeWithDefaults(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.initializeWithDefaults(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onApplicationEnterForeground() {
        initializers.onEnterForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onApplicationEnterBackground() {
        initializers.onEnterBackground()
    }

    abstract fun releaseLoggingTree(): ReleaseLoggingTree
}