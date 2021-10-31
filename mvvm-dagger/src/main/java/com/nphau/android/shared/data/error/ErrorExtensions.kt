/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.data.error

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable?.isNetworkException(): Boolean = this is RuntimeException
        || this is UnknownHostException
        || this is SocketTimeoutException
        || this?.cause is UnknownHostException
        || this?.cause is SocketTimeoutException
        || this?.cause is ConnectException

fun Throwable?.isUnauthorized(): Boolean {
    return this is HttpException && ErrorHandlers.UNAUTHORIZED_ERRORS.contains(this.code())
}

fun Throwable?.toServerError(): ServerError {
    if (this is HttpException) {
        val errorBody = response()?.errorBody()?.string()
        val (errorCode, message) = ErrorHandlers.parseServerError(errorBody)
        return ServerError.error(errorCode, message, this)
    }
    return ServerError.error(this)
}