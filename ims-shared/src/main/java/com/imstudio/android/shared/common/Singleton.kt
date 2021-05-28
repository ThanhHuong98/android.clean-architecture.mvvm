/*
 * Created by IMStudio on 5/11/21 10:36 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common

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
