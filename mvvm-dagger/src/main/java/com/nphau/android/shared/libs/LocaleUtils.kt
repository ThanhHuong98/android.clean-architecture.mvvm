/*
 * Created by nphau on 31/10/2021, 21:43
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.libs

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.nphau.android.shared.common.extensions.getTAG
import com.nphau.android.shared.common.functional.tryOrNull
import java.util.*

class LocaleUtils private constructor(context: Context) : ContextWrapper(context) {

    private var DEFAULT_LANGUAGE: String? = null

    private val prefs: SharedPreferences

    private val systemLanguage: String
        get() {
            return tryOrNull {
                Resources.getSystem().configuration.locale.language
            } ?: DEFAULT_LANGUAGE_EN
        }

    /*
     * get current language
     * */
    fun getLanguage(): String {
        return prefs.getString(DEFAULT_LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE_VI
    }

    init {
        DEFAULT_LANGUAGE_KEY = String.format("%s_%s", context.packageName, getTAG())
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context)
        this.DEFAULT_LANGUAGE = systemLanguage
    }

    private constructor(context: Context, firstLauncher: String) : this(context) {
        this.DEFAULT_LANGUAGE = firstLauncher
    }

    /*
     * get current language with default
     * */
    fun getLanguage(language: String): String {
        return prefs.getString(DEFAULT_LANGUAGE_KEY, language) ?: DEFAULT_LANGUAGE_VI
    }

    /*
     *  persist language in prefs
     * */
    private fun persist(language: String) {
        prefs.edit().putString(DEFAULT_LANGUAGE_KEY, language).apply()
    }

    fun setLocale(context: Context?): Context? {
        return updateResources(context, getLanguage())
    }

    fun setLocale(context: Context, language: String) {
        persist(language)
        updateResources(context, language)
    }

    /*
     *  Update resource
     * */
    private fun updateResources(refContext: Context?, language: String): Context? {
        var context = refContext
        if (context != null) {
            try {
                val locale = Locale(language)
                Locale.setDefault(locale)
                val res = context.resources
                val configuration = Configuration(res.configuration)
                configuration.setLocale(locale)
                context = context.createConfigurationContext(configuration)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return context
    }

    companion object {

        private lateinit var DEFAULT_LANGUAGE_KEY: String

        const val DEFAULT_LANGUAGE_VI = "vi"
        const val DEFAULT_LANGUAGE_EN = "en"
        private var localeUtils: LocaleUtils? = null

        @JvmStatic
        fun initializeWithDefaults(context: Context) {
            if (localeUtils == null)
                localeUtils = LocaleUtils(context)
            localeUtils!!.setLocale(context)
        }

        @JvmStatic
        fun initializeWithDefaults(context: Context, firstLauncher: String) {
            if (localeUtils == null)
                localeUtils = LocaleUtils(context, firstLauncher)
            localeUtils!!.setLocale(context)
        }

        @JvmStatic
        fun self(): LocaleUtils {
            return localeUtils!!
        }
    }

}
