package com.imstudio.android.shared.screens.adapters

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imstudio.android.shared.common.executors.IO_EXECUTOR

abstract class CoreAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, RecyclerView.ViewHolder>(
        AsyncDifferConfig.Builder<T>(diffCallback)
            .setBackgroundThreadExecutor(IO_EXECUTOR)
            .build()
    ) {

    open fun get(position: Int): T {
        if (position < 0 || position > itemCount)
            throw NullPointerException("Can not find element at $position while itemCount is $itemCount")
        return super.getItem(position)
    }

    fun isEmpty() = itemCount <= 0

    fun isNotEmpty() = !isEmpty()

}
