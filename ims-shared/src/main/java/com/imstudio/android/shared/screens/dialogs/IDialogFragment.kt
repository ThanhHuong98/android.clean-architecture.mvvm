/*
 * Created by IMStudio on 5/29/21 7:45 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/29/21 7:45 AM
 */

package com.imstudio.android.shared.screens.dialogs

import android.app.Activity
import com.imstudio.android.shared.common.extensions.hideKeyboardIfNeed

interface IDialogFragment {
    fun hideKeyboardIfNeed(activity: Activity?) = activity?.hideKeyboardIfNeed()
}