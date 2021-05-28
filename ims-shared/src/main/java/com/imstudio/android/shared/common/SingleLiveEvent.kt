/*
 * Created by IMStudio on 5/11/21 10:36 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MutableLiveData<T>() {

    companion object {
        val TAG: String = SingleLiveEvent::class.java.name
    }

    private val pending = AtomicBoolean(false)

    @SuppressLint("LogNotTimber")
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers())
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        // Observe the internal MutableLiveData
        super.observe(owner, { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}
