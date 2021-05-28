package com.imstudio.app.di.module

import androidx.lifecycle.ViewModel
import com.imstudio.android.shared.di.qualifiers.ViewModelKey
import com.imstudio.app.screens.activities.MainActivity
import com.imstudio.app.vm.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}