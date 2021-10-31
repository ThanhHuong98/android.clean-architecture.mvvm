/*
 * Created by nphau on 31/10/2021, 21:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.common.extensions

import android.content.res.Resources
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.nphau.android.shared.R
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