/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Target(
        AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD,
        AnnotationTarget.VALUE_PARAMETER
)
annotation class SchedulerNewThread
