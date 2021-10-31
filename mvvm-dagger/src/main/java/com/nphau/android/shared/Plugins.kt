/*
 * Created by IMStudio on 5/11/21 10:46 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:46 AM
 */

package com.nphau.android.shared

import javax.inject.Inject

class Plugins @Inject constructor(private val initializes: Set<@JvmSuppressWildcards PluginInitializer>) {

    fun initWith(application: com.nphau.android.Launcher) {
        initializes.forEach { plugin ->
            plugin.initialize(application)
        }
    }

    fun onEnterBackground() = initializes.forEach { it.onEnterBackground() }

    fun onEnterForeground() = initializes.forEach { it.onEnterForeground() }

}
