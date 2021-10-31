/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.libs.logger

import android.os.Build
import android.util.Log
import com.nphau.android.shared.data.error.ErrorHandlers
import timber.log.Timber

class DebugLoggingTree : Timber.DebugTree() {

    companion object {
        @JvmStatic
        val BLACK_LIST_OF_DEVICES = arrayListOf("HUAWEI", "samsung")
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        if (ErrorHandlers.isIgnoreException(throwable))
            return

        // Workaround for devices that doesn't show lower priority logs
        if (BLACK_LIST_OF_DEVICES.any { it == Build.MANUFACTURER }) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
                super.log(Log.ERROR, tag, message, throwable)
            else
                super.log(priority, tag, message, throwable)
        } else {
            super.log(priority, tag, message, throwable)
        }
    }

}
