package com.imstudio.app.screens.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.imstudio.android.shared.common.extensions.browse
import com.imstudio.android.shared.common.extensions.safeClick
import com.imstudio.android.shared.screens.activities.DaggerViewBindingActivity
import com.imstudio.app.databinding.ActivityMainBinding
import com.imstudio.app.vm.MainViewModel
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
            browse("https://imstudio.medium.com/")
        }
    }

}