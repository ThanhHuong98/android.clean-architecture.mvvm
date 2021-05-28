package com.imstudio.app

import com.imstudio.android.IMSShared
import com.imstudio.android.shared.libs.logger.ReleaseLoggingTree
import com.imstudio.app.di.DaggerAppComponent
import com.imstudio.app.libs.CrashlyticsLoggingTree
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class IMSApp : IMSShared() {

    override fun releaseLoggingTree(): ReleaseLoggingTree = CrashlyticsLoggingTree()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}