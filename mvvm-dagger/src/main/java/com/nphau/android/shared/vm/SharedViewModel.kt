/*
 * Created by IMStudio on 4/18/21 9:19 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 9:16 AM
 */

package com.nphau.android.shared.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nphau.android.shared.common.functional.Result
import com.nphau.android.shared.data.error.ServerError
import com.nphau.android.shared.data.error.toServerError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class SharedViewModel : ViewModel(), HasDisposableManager {

    private var compositeDisposable = CompositeDisposable()

    // Use for common task, do not care about action after that
    private val _commonTask: MutableLiveData<Result<Boolean, ServerError>> =
        MutableLiveData()
    val commonTask: LiveData<Result<Boolean, ServerError>> = _commonTask

    override fun getCompositeDisposable(): CompositeDisposable {
        if (compositeDisposable.isDisposed)
            compositeDisposable = CompositeDisposable()
        return compositeDisposable
    }

    override fun addToDisposable(disposable: Disposable) {
        this.compositeDisposable.add(disposable)
    }

    override fun disposeAll() {
        getCompositeDisposable().clear()
    }

    override fun onCleared() {
        disposeAll()
        super.onCleared()
    }

    protected fun dispatchLoading() = _commonTask.postValue(Result.Loading)

    protected fun dispatchSuccess() = _commonTask.postValue(Result.Success(true))

    protected fun dispatchError(throwable: Throwable) {
        _commonTask.postValue(Result.Failure(throwable.toServerError()))
    }
}
