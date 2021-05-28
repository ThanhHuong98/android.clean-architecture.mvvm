package com.imstudio.app.di

import android.app.Application
import com.imstudio.android.shared.di.modules.CommonModule
import com.imstudio.app.IMSApp
import com.imstudio.app.di.module.MainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MainModule::class,
        CommonModule::class,
        AndroidSupportInjectionModule::class]
)
interface AppComponent : AndroidInjector<IMSApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}