/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import android.content.res.Resources
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imstudio.android.shared.R
import com.imstudio.android.shared.screens.listeners.OnItemClickListener
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Set an onclick listener
 */

inline fun <T : View> T.safeClick(crossinline block: () -> Unit): Disposable {
    return RxView.clicks(this)
        .throttleFirst(850, TimeUnit.MILLISECONDS)
        .subscribe({ block() }, { })
}

inline fun <T : View> T.safeClick(delayInMills: Long, crossinline block: () -> Unit): Disposable {
    return RxView.clicks(this)
        .throttleFirst(delayInMills, TimeUnit.MILLISECONDS)
        .subscribe({ block() }, { })
}

fun RecyclerView.defaultDivider() = setDivider(
    orientation = DividerItemDecoration.VERTICAL,
    dividerResId = R.drawable.all_divider_soft_line
)

fun RecyclerView.setDivider(
    orientation: Int = DividerItemDecoration.VERTICAL, dividerResId: Int? = null
) {
    val dividerItemDecoration = DividerItemDecoration(this.context, orientation)
    dividerResId?.let { resId ->
        this.context.getDrawableCompat(resId)
            ?.let { divider ->
                dividerItemDecoration.setDrawable(divider)
            }
    }
    this.addItemDecoration(dividerItemDecoration)
}

fun RecyclerView.addOnItemClickListener(callback: (view: View, position: Int, isSelected: Boolean) -> Unit) {
    addOnItemTouchListener(OnItemClickListener(context, callback))
}

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


@BindingAdapter("goneUnless")
fun View.goneUnless(conditionToVisible: Boolean? = true) {
    visibility = if (conditionToVisible == null || conditionToVisible == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("invisibleUnless")
fun View.invisibleUnless(conditionToVisible: Boolean? = true) {
    visibility = if (conditionToVisible == null || conditionToVisible == true) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}