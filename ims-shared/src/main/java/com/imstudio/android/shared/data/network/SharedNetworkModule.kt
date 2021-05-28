/*
 * Created by IMStudio on 5/11/21 10:39 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.data.network

import com.imstudio.android.shared.data.factory.NullOnEmptyConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class SharedNetworkModule {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(getOkHttpClient())
            .addConverterFactory(NullOnEmptyConverterFactory.create())
            .addConverterFactory(requireGSON())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    inline fun <reified S> getService(): S = getRetrofit().create(S::class.java)

    open fun requireGSON(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().create())

    open fun getOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder().apply {
            this.followRedirects(true)
                .followSslRedirects(true)
                .readTimeout(readTimeout(), TimeUnit.SECONDS)
                .writeTimeout(writeTimeout(), TimeUnit.SECONDS)
                .connectTimeout(connectTimeout(), TimeUnit.SECONDS)
            modifiedInterceptors().forEach { this.addInterceptor(it) }
            modifiedNetworkInterceptors().forEach { this.addNetworkInterceptor(it) }
        }
        return okHttpBuilder.build()
    }

    abstract fun getBaseUrl(): String

    open fun writeTimeout(): Long = NetworkConfig.WRITE_TIMEOUT

    open fun readTimeout(): Long = NetworkConfig.READ_TIMEOUT

    open fun connectTimeout(): Long = NetworkConfig.CONNECT_TIMEOUT

    abstract fun modifiedInterceptors(): List<Interceptor>

    open fun modifiedNetworkInterceptors(): List<Interceptor> = emptyList()

}
