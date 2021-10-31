/*
 * Created by nphau on 31/10/2021, 21:44
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.common.delegates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nphau.android.shared.common.functional.Provider
import com.nphau.android.shared.common.functional.get
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T : Any> liveData(
    noinline provider: Provider<T>
) = LiveDataDelegate(provider.get())

class LiveDataDelegate<T : Any>(
    initialState: T, private val liveData: MutableLiveData<T> = MutableLiveData(initialState)
) : ReadWriteProperty<Any, LiveData<T>> {

    override fun setValue(thisRef: Any, property: KProperty<*>, value: LiveData<T>) =
        liveData.postValue(value.value)

    override operator fun getValue(thisRef: Any, property: KProperty<*>): LiveData<T> = liveData

}

fun <T> T.asLiveData(): LiveData<T> = MutableLiveData(this)