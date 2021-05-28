/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.imstudio.android.shared.screens.activities.DaggerViewBindingActivity
import java.lang.reflect.ParameterizedType

internal fun Class<*>.checkMethod(): Boolean {
    return try {
        getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        true
    } catch (ex: Exception) {
        false
    }
}

internal fun Any.findClass(): Class<*> {
    var javaClass: Class<*> = this.javaClass
    var result: Class<*>? = null
    while (result == null || !result.checkMethod()) {
        result = (javaClass
            .genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.firstOrNull {
                if (it is Class<*>) {
                    it.checkMethod()
                } else {
                    false
                }
            } as? Class<*>
        javaClass = javaClass.superclass
    }
    return result
}

internal fun <V : ViewBinding> Class<*>.getBinding(
    layoutInflater: LayoutInflater,
    container: ViewGroup?
): V {
    return try {
        @Suppress("UNCHECKED_CAST")
        getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).invoke(null, layoutInflater, container, false) as V
    } catch (ex: Exception) {
        throw RuntimeException("The ViewBinding inflate function has been changed.", ex)
    }
}

internal fun <V : ViewBinding> Class<*>.getBinding(layoutInflater: LayoutInflater): V {
    return try {
        @Suppress("UNCHECKED_CAST")
        getMethod(
            "inflate",
            LayoutInflater::class.java
        ).invoke(null, layoutInflater) as V
    } catch (ex: Exception) {
        throw RuntimeException("The ViewBinding inflate function has been changed.", ex)
    }
}

internal fun <V : ViewBinding> DaggerViewBindingActivity<V>.getBinding(): V {
    return findClass().getBinding(layoutInflater)
}