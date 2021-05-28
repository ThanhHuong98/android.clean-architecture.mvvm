/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.di.modules

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imstudio.android.shared.PluginInitializer
import com.imstudio.android.shared.di.DaggerViewModelFactory
import com.imstudio.android.shared.libs.prefs.PrefsUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module(includes = [SchedulerModule::class, BindsModule::class])
object CommonModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideClipboardManager(context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

}

@Module
interface BindsModule {

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoSet
    fun sharedPrefsServiceInitializer(initializer: PrefsUtils): PluginInitializer
}