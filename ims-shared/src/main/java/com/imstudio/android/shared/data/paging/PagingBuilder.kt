/*
 * Created by IMStudio on 5/28/21 10:19 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/28/21 10:19 PM
 */

package com.imstudio.android.shared.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

@DslMarker
internal annotation class PagingBuilderDSL

data class PagingConfigBuilder(
    var pageSize: Int = 10,
    var initialLoadSize: Int = 10,
    var prefetchDistance: Int = 10,
    var enablePlaceholders: Boolean = true
)

@PagingBuilderDSL
object PagingBuilder {

    @PagingBuilderDSL
    fun pagingConfig(builder: (PagingConfigBuilder.() -> Unit)? = null): PagingConfig {
        val paging = PagingConfigBuilder()
        builder?.let { paging.apply(it) }
        return PagingConfig(
            pageSize = paging.pageSize,
            initialLoadSize = paging.initialLoadSize,
            prefetchDistance = paging.prefetchDistance,
            enablePlaceholders = paging.enablePlaceholders
        )
    }
}

fun <Key : Any, Value : Any> createPagerConfig(
    pagingSource: PagingSource<Key, Value>,
    pagingConfig: PagingConfig
): Pager<Key, Value> = Pager(config = pagingConfig, pagingSourceFactory = { pagingSource })