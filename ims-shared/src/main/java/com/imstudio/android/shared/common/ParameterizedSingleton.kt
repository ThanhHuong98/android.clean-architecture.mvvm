/*
 * Created by IMStudio on 5/11/21 10:36 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common

open class ParameterizedSingleton<out T, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(argument: A): T {
        val checkInstance = instance
        if (checkInstance != null) return checkInstance

        return synchronized(this) {
            val synchronizedCheck = instance
            if (synchronizedCheck != null) return synchronizedCheck
            else {
                val createdInstance = creator!!(argument)
                instance = createdInstance
                creator = null
                createdInstance
            }
        }
    }
}
