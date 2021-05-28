/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import android.content.res.TypedArray
import android.os.Parcelable
import android.util.Log
import com.imstudio.android.shared.common.functional.Predicate
import com.imstudio.android.shared.common.functional.tryOrNull
import com.imstudio.android.shared.common.functional.unit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.util.concurrent.TimeUnit

// 2 variables
inline fun <T1 : Any, T2 : Any, R : Any> letAllNotNull(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> R?
): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <reified T : Any> T.getTAG(): String = T::class.java.name

inline fun <reified T : Any> T?.letIfNotNullAnd(condition: Predicate<T>, callBack: unit<T>) {
    if (this != null && condition.invoke(this)) {
        callBack.invoke(this)
    }
}

@JvmSynthetic
inline fun <T> T.letIf(given: Boolean?, letIf: T.() -> Unit): T {
    if (given == true) {
        this.apply { letIf() }
    }
    return this
}

// region [G-SON]
internal val gson by lazy { GsonBuilder().disableHtmlEscaping().create() }

internal inline fun <reified T> String.fromJson(): T = gson.fromJson(this, T::class.java)

internal inline fun <reified T> Gson.fromJson(json: String): T =
    this.fromJson(json, object : TypeToken<T>() {}.type)

internal fun Any.toJson(): String = gson.toJson(this)

/**
 * Parse serializer object into readable Json
 */
fun Parcelable.toJson(prettyPrinting: Boolean = false): String? = tryOrNull {
    GsonBuilder()
        .apply { if (prettyPrinting) setPrettyPrinting() }
        .create().toJson(this)
}

// endregion

inline fun <T : TypedArray?, R> T.use(block: (T) -> R): R {
    var recycled = false
    try {
        return block(this)
    } catch (e: Exception) {
        recycled = true
        try {
            this?.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw e
    } finally {
        if (!recycled) {
            this?.recycle()
        }
    }
}

infix fun <T> Boolean.Then(action: () -> T): T? {
    return if (this)
        action.invoke()
    else null
}

infix fun <T> T?.or(default: T): T = this ?: default
infix fun <T> T?.Else(compute: () -> T): T = this ?: compute()

/**
 * Pass any exception while processing block
 * When use this method meaning statement no need to handle exception, exception now not critical
 * @return last statement result or null if throw any exceptions
 */
infix fun <T, R : Any> T.passException(block: () -> R): R? {
    return try {
        block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/*
* This is useful for generating crude benchmarks of operations so you can see roughly
* how long a block of code takes to execute. It’s not scientific,
* but sometimes it’s enough to make decisions on where bottlenecks might lie.
* */
inline fun <T> measureExecution(
    logMessage: String,
    logLevel: Int = Log.DEBUG,
    function: () -> T
): T {
    val startTime = System.nanoTime()
    return function.invoke().also {
        val difference = System.nanoTime() - startTime
        Timber.log(logLevel, "$logMessage; took ${TimeUnit.NANOSECONDS.toMillis(difference)}ms")
    }
}

// region [Mumeric]
fun Int.isEven(): Boolean = (this % 2) == 0

/* return 1 if true, return 0 if null or false   */
fun Boolean?.toInt(): Int = if (this == true) 1 else 0
fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0L
//endregion