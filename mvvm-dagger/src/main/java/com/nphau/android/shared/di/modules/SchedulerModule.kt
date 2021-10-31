/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.di.modules

import com.nphau.android.shared.di.qualifiers.SchedulerComputationThread
import com.nphau.android.shared.di.qualifiers.SchedulerIoThread
import com.nphau.android.shared.di.qualifiers.SchedulerMainThread
import com.nphau.android.shared.di.qualifiers.SchedulerNewThread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object SchedulerModule {

    @Provides
    @Singleton
    @SchedulerComputationThread
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @SchedulerIoThread
    @Singleton
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    @SchedulerMainThread
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    @SchedulerComputationThread
    fun provideTrampolineThreadScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Provides
    @Singleton
    @SchedulerNewThread
    fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()
}
