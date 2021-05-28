/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.data.network

object NetworkConfig {
    const val CACHE_MAX_SIZE = 50L * 1024L * 1024L // 10Mb
    const val WRITE_TIMEOUT = 60L
    const val READ_TIMEOUT = 60L
    const val CONNECT_TIMEOUT = 60L
}
