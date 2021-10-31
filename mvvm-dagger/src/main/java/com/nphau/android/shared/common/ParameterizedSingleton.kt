/*
 * Created by nphau on 31/10/2021, 21:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.common

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
