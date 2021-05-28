package com.imstudio.app.libs

import android.util.Log
import com.imstudio.android.shared.libs.logger.ReleaseLoggingTree
import com.google.firebase.crashlytics.FirebaseCrashlytics

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
