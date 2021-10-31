package com.nphau.app.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.nphau.android.shared.common.extensions.browse
import com.nphau.android.shared.common.extensions.safeClick
import com.nphau.android.shared.screens.activities.DaggerViewBindingActivity
import com.nphau.app.databinding.ActivityMainBinding
import com.nphau.app.vm.MainViewModel
import javax.inject.Inject

class MainActivity : DaggerViewBindingActivity<ActivityMainBinding>() {

    // region [Inject]
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    // endregion

    override fun onSyncViews(savedInstanceState: Bundle?) {
        super.onSyncViews(savedInstanceState)
        binding.buttonWelcome.safeClick {
            browse("https://nphau.medium.com/")
        }
        binding.toolBar.onLeftClickListener(::finish)
    }

}