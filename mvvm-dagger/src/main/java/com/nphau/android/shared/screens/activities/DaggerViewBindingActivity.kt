package com.nphau.android.shared.screens.activities

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.nphau.android.shared.common.extensions.getBinding

open class DaggerViewBindingActivity<VB : ViewBinding> : DaggerSharedActivity() {

    // Binding variable to be used for accessing views.
    protected lateinit var binding: VB

    override fun onSyncViews(savedInstanceState: Bundle?) {
        super.onSyncViews(savedInstanceState)
        binding = getBinding()
        setContentView(binding.root)
    }


}