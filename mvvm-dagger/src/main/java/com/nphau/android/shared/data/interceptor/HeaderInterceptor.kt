/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.nphau.android.shared.data.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        /*
         * Get the response code from the request. In this scenario we are only handling
         * unauthorised and server unavailable error scenario
         * */
        val response = chain.proceed(
            request.newBuilder()
                .headers(createHeaders())
                .method(request.method, request.body)
                .build()
        )
        return response
    }

    companion object {
        const val HEADER_ACCEPT = "Accept"
        const val HEADER_PLATFORM = "x-platform"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
        const val HEADER_AUTHORIZATION_BEARER = "Authorization"
        const val HEADER_CONTENT_TYPE_JSON = "application/json; charset=UTF-8"
    }

    abstract fun createHeaders(): Headers

    protected open fun getAccessToken(): String = ""

    protected open fun authorizationType(): String = HEADER_AUTHORIZATION_BEARER

}
