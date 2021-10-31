package com.nphau.app.di.module

import androidx.lifecycle.ViewModel
import com.nphau.android.shared.di.qualifiers.ViewModelKey
import com.nphau.app.screens.MainActivity
import com.nphau.app.vm.MainViewModel
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