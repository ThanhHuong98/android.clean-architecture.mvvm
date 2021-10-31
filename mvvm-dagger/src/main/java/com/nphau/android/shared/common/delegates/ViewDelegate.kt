/*
 * Created by nphau on 31/10/2021, 21:45
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.common.delegates

import android.widget.TextView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun TextView.text(): ReadWriteProperty<Any, String> =
    object : ReadWriteProperty<Any, String> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ): String = text.toString()

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>, value: String
        ) {
            text = value
        }
    }
