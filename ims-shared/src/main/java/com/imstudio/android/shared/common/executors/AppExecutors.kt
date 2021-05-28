/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.executors

import android.os.Handler
import android.os.Looper
import com.imstudio.android.shared.common.Singleton
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */

val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()

open class AppExecutors(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor
) {
    @Inject
    constructor() : this(
        IO_EXECUTOR,
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )

    companion object : Singleton<AppExecutors>(::AppExecutors)

    fun diskIO(): Executor = diskIO

    fun networkIO(): Executor = networkIO

    fun mainThread(): Executor = mainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}
