/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.libs.logger

import android.util.Log
import com.imstudio.android.shared.data.error.ErrorHandlers
import timber.log.Timber

abstract class ReleaseLoggingTree : Timber.Tree() {

    companion object {

        @JvmStatic
        val CRASHLYTICS_KEY_PRIORITY = "priority"

        @JvmStatic
        val CRASHLYTICS_KEY_TAG = "tag"

        @JvmStatic
        val CRASHLYTICS_KEY_MESSAGE = "message"
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        applyKeys(priority, tag ?: System.currentTimeMillis().toString(), message)

        if (ErrorHandlers.isIgnoreException(throwable)) {
            return
        }
        if (priority >= Log.ERROR)
            logException(throwable ?: Exception(message))
        else
            logOther(priority, tag, message)
    }

    abstract fun applyKeys(priority: Int, tag: String?, message: String)

    abstract fun logOther(priority: Int, tag: String?, message: String)

    abstract fun logException(throwable: Throwable?)
}
