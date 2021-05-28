/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.data.interceptor

import com.imstudio.android.shared.libs.LocaleUtils
import okhttp3.Headers

/**
 * @Use custom headers for okHttp
 * */
open class UnAuthorizationHeaderInterceptor : HeaderInterceptor() {

    override fun createHeaders(): Headers {
        val header = Headers.Builder()
            .add(HEADER_ACCEPT_LANGUAGE, LocaleUtils.self().getLanguage())
            .add(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_JSON)
        return header.build()
    }

}
