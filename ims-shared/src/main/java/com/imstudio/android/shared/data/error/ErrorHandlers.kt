/*
 * Created by IMStudio on 5/11/21 10:38 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.data.error

import org.json.JSONObject
import java.net.HttpURLConnection

object ErrorHandlers {

    val SERVER_ERRORS = listOf(
        HttpURLConnection.HTTP_INTERNAL_ERROR,
        HttpURLConnection.HTTP_NOT_IMPLEMENTED,
        HttpURLConnection.HTTP_BAD_GATEWAY,
        HttpURLConnection.HTTP_UNAVAILABLE,
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT
    )

    val UNAUTHORIZED_ERRORS = listOf(
        HttpURLConnection.HTTP_UNAUTHORIZED,
        HttpURLConnection.HTTP_FORBIDDEN
    )

    @JvmStatic
    fun parseServerError(error: String?): Pair<Int, String?> {
        var errorMessage: String? = null
        var errorCode = SERVER_ERRORS.first()
        try {
            if (error != null) {
                val jsonObject = JSONObject(error)
                if (jsonObject.has("message")) {
                    errorMessage = jsonObject.getString("message")
                } else if (jsonObject.has("errors")) {
                    val errors = jsonObject.getJSONObject("errors")
                    if (errors.has("message"))
                        errorMessage = errors.getString("message")
                } else if (jsonObject.has("")) {
                    errorMessage = jsonObject.getString("")
                }
                if (jsonObject.has("status"))
                    errorCode = jsonObject.getInt("status")

                if (jsonObject.has("code"))
                    errorCode = jsonObject.getInt("code")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(errorCode, errorMessage)
    }

    @JvmStatic
    fun isIgnoreException(throwable: Throwable?): Boolean {
        return throwable.isNetworkException()
    }

}
