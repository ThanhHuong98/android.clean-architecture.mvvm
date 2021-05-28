/*
 * Created by IMStudio on 5/11/21 10:36 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:35 AM
 */

package com.imstudio.android

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
import com.imstudio.android.shared.IMSEnvironment
import com.imstudio.android.shared.IMSPluginInitializer
import com.imstudio.android.shared.libs.LocaleUtils
import com.imstudio.android.shared.libs.NetworkUtils
import com.imstudio.android.shared.libs.logger.DebugLoggingTree
import com.imstudio.android.shared.libs.logger.ReleaseLoggingTree
import com.uber.rxdogtag.RxDogTag
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

abstract class IMSShared : DaggerApplication(), LifecycleObserver {

    @Inject
    lateinit var initializers: IMSPluginInitializer

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

        if (IMSEnvironment.isDebug()) {
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