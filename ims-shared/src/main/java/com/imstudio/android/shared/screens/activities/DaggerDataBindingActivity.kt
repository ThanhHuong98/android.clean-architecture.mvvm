package com.imstudio.android.shared.screens.activities

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.imstudio.android.shared.common.extensions.activityBinding

open class DaggerDataBindingActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : DaggerSharedActivity() {

    protected val binding: B by activityBinding(layoutId)

    override fun onSyncViews(savedInstanceState: Bundle?) {
        super.onSyncViews(savedInstanceState)
        binding.lifecycleOwner = this
    }
}
