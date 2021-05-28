/*
 * Created by IMStudio on 5/11/21 10:36 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.EXPRESSION
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Comment(
    val message: String
)
