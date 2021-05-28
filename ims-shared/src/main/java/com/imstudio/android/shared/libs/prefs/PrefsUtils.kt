/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:35 AM
 */

package com.imstudio.android.shared.libs.prefs

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.imstudio.android.IMSShared
import com.imstudio.android.shared.PluginInitializer
import com.imstudio.android.shared.common.extensions.getTAG
import com.imstudio.android.shared.common.functional.tryOrNull
import com.imstudio.android.shared.libs.encryption.EncryptionUtils
import com.imstudio.android.shared.libs.encryption.toBASE64
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsUtils @Inject constructor() : PluginInitializer {

    override fun tag(): String = getTAG()

    private val gSon by lazy { Gson() }
    private lateinit var preferences: SharedPreferences

    companion object {
        private const val KEY_PREFS = "shared_prefs"
    }

    @Synchronized
    override fun initialize(application: IMSShared) {
        super.initialize(application)
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        preferences = EncryptedSharedPreferences.create(
            application, KEY_PREFS, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onEnterBackground() = Unit

    override fun onEnterForeground() = Unit

    fun <T> put(key: String, value: T) {
        preferences.apply {
            when (value) {
                is Int -> edit().putInt(key, value as Int).apply()
                is Long -> edit().putLong(key, value as Long).apply()
                is Boolean -> edit().putBoolean(key, value as Boolean).apply()
                is String -> edit().putString(key, (value as String).toBASE64()).apply()
                else -> edit().putString(key, Gson().toJson(value).toBASE64()).apply()
            }
        }
    }

    fun <T> get(key: String, type: Type): T? {
        return tryOrNull { gSon.fromJson<T>(getString(key), type) }
    }

    fun <T> get(key: String, classOfT: Class<T>): T? {
        return tryOrNull { gSon.fromJson(getString(key), classOfT) }
    }

    inline fun <reified T> get(key: String, defaultValue: Any? = null): T? {
        return (when (T::class) {
            Boolean::class -> getBoolean(key, (defaultValue ?: false) as Boolean) as T
            Int::class -> getInt(key, (defaultValue ?: Int.MAX_VALUE) as Int) as T
            Long::class -> getLong(key, (defaultValue ?: Long.MAX_VALUE) as Long) as T
            Float::class -> getFloat(key, (defaultValue ?: Float.MAX_VALUE) as Float) as T
            String::class -> getString(key, (defaultValue ?: "") as String) as T
            else -> get(key, T::class.java)
        } ?: defaultValue) as T?
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        if (contains(key))
            return preferences.getBoolean(key, defaultValue)
        return false
    }

    fun getInt(key: String, defaultValue: Int = Int.MAX_VALUE): Int {
        if (contains(key))
            return preferences.getInt(key, defaultValue)
        return defaultValue
    }

    fun getFloat(key: String, defaultValue: Float = Float.MAX_VALUE): Float {
        if (contains(key))
            return preferences.getFloat(key, defaultValue)
        return defaultValue
    }

    fun getLong(key: String, defaultValue: Long = Long.MAX_VALUE): Long {
        if (contains(key))
            return preferences.getLong(key, defaultValue)
        return defaultValue
    }

    fun getString(key: String, defaultValue: String = ""): String {
        if (contains(key))
            return EncryptionUtils.decryptFromBase64(preferences.getString(key, defaultValue))
        return defaultValue
    }

    fun contains(key: String): Boolean = preferences.contains(key)

}