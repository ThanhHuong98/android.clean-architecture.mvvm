/*
 * Created by nphau on 31/10/2021, 22:23
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.flags

@Target(
    AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class OnLifecycleStart