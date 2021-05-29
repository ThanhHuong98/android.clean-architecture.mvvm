package com.imstudio.android.shared.data.paging

import androidx.paging.rxjava2.RxPagingSource

abstract class RxCorePagingSource<Key : Any, Value : Any> : RxPagingSource<Key, Value>() {

    open fun beginPageIndex(): Int = 0

    fun <Value : Any> result(
        currentPageIndex: Int,
        results: List<Value> = emptyList()
    ): LoadResult<Int, Value> {
        return LoadResult.Page(
            data = results,
            prevKey = if (currentPageIndex == beginPageIndex()) null else currentPageIndex - 1,
            nextKey = if (results.isEmpty()) null else currentPageIndex + 1,
            itemsAfter = LoadResult.Page.COUNT_UNDEFINED,
            itemsBefore = LoadResult.Page.COUNT_UNDEFINED
        )
    }

}
