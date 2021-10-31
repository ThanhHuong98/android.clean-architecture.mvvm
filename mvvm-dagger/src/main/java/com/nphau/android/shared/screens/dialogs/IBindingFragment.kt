/*
 * Created by IMStudio on 5/29/21 8:02 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/29/21 8:02 AM
 */

package com.nphau.android.shared.screens.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.nphau.android.shared.common.functional.BindingInflater

interface IBindingFragment<VB : ViewBinding> {

    val bindingInflater: BindingInflater<VB>

    fun inflate(context: Context?) = bindingInflater.invoke(LayoutInflater.from(context))

}