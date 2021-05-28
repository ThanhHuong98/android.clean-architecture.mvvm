/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.di.qualifiers

import javax.inject.Scope

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Scopes

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScoped

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class NestedFragmentScoped
