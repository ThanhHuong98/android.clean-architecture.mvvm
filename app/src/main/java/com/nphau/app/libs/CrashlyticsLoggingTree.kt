/*
 * Created by nphau on 31/10/2021, 21:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:39
 */

package com.nphau.app.libs

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nphau.android.shared.libs.logger.ReleaseLoggingTree

class CrashlyticsLoggingTree : ReleaseLoggingTree() {

    override fun applyKeys(priority: Int, tag: String?, message: String) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
        crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag ?: "")
        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
        // crashlytics.setUserId(GoogleService.fireBaseUserId())
    }

    override fun logException(throwable: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        if (throwable != null) {
            crashlytics.recordException(throwable)
        }
    }

    override fun logOther(priority: Int, tag: String?, message: String) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        val priorityName = when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "D"
        }
        crashlytics.log("$priorityName/$tag:$message")
    }
}
