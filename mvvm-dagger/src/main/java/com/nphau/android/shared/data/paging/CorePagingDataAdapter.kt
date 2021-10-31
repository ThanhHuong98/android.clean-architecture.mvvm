/*
 * Created by nphau on 31/10/2021, 22:08
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.data.paging

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class CorePagingDataAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    fun lastItem(): T? = getItem(itemCount - 1)

    open fun get(position: Int): T? = super.getItem(position)

    fun isEmpty(): Boolean = itemCount == 0
}
