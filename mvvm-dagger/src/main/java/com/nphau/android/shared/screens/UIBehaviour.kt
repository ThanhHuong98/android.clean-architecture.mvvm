package com.nphau.android.shared.screens

import android.os.Bundle

interface UIBehaviour {

    fun showError(message: String?)

    fun onSyncViews(savedInstanceState: Bundle?)

    fun onSyncEvents()

    fun onSyncData()

    fun makeVibrator()

    fun doNotCare()

    fun showLoading(isShow: Boolean, timeout: Long = 2_000)

}
