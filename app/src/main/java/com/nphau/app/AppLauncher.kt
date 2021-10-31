package com.nphau.app

import com.nphau.android.Launcher
import com.nphau.android.shared.libs.logger.ReleaseLoggingTree
import com.nphau.app.di.DaggerAppComponent
import com.nphau.app.libs.CrashlyticsLoggingTree
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AppLauncher : Launcher() {

    override fun releaseLoggingTree(): ReleaseLoggingTree = CrashlyticsLoggingTree()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}