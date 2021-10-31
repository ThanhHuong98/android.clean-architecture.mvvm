/*
 * Created by nphau on 31/10/2021, 21:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.common

open class Singleton<out T : Any>(creator: () -> T) {

    private var creator: (() -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(): T {
        val i = instance
        if (i != null)
            return i
        return synchronized(this) {
            val i2 = instance
            if (i2 != null)
                i2
            else {
                val created = creator!!()
                instance = created
                creator = null
                created
            }
        }
    }
}
