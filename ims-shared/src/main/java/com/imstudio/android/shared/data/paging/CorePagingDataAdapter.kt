package com.imstudio.android.shared.data.paging

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class CorePagingDataAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    fun lastItem(): T? = getItem(itemCount - 1)

    open fun get(position: Int): T? = super.getItem(position)

    fun isEmpty(): Boolean = itemCount == 0
}
