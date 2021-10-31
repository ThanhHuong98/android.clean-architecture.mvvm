package com.nphau.android.shared.vm

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface HasDisposableManager {
    fun getCompositeDisposable(): CompositeDisposable
    fun addToDisposable(disposable: Disposable)
    fun disposeAll()
}
