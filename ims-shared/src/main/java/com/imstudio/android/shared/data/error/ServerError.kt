/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.data.error

import java.net.HttpURLConnection

data class ServerError(
    val errorCode: Int = HttpURLConnection.HTTP_INTERNAL_ERROR,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Throwable(message, cause) {

    companion object {

        private const val ERROR_CODE_DEFAULT = HttpURLConnection.HTTP_INTERNAL_ERROR

        fun error(throwable: Throwable?): ServerError {
            return ServerError(cause = throwable, message = throwable?.message)
        }

        fun error(errorMessage: String?): ServerError {
            return ServerError(message = errorMessage)
        }

        fun error(errorCode: Int, message: String?): ServerError {
            return ServerError(errorCode, message)
        }

        fun error(errorCode: Int, message: String?, cause: Throwable?): ServerError {
            return ServerError(errorCode, message, cause)
        }

        fun error(message: String?, cause: Throwable?): ServerError {
            return error(ERROR_CODE_DEFAULT, message, cause)
        }
    }

    fun getErrorMessage(): String? = message ?: cause?.message

    fun isServerError(): Boolean {
        return ErrorHandlers.SERVER_ERRORS.contains(errorCode)
    }

    override fun toString(): String {
        return "[$errorCode] $message"
    }
}
