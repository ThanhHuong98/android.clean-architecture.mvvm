/*
 * Created by nphau on 31/10/2021, 21:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

// Use this annotation so you can call it from Java code like FormatUtils.
@file:JvmName("FormatUtils")

package com.nphau.android.shared.common.extensions

import android.graphics.Color
import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.parseAsInt(): Int {
    return try {
        toInt()
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}

fun String.replaceBreakLine(): String {
    if (contains("\n"))
        return replace("\n", "<br/>")
    return this
}

fun String.formatHtml(): Spanned {
    if (!isNullOrEmpty()) {
        if (contains("\n"))
            return HtmlCompat
                .fromHtml(replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    return HtmlCompat
        .fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

val String.containsLatinLetter: Boolean
    get() = matches(Regex(".*[A-Za-z].*"))

val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.isAlphanumeric: Boolean
    get() = matches(Regex("[A-Za-z0-9]*"))

val String.hasLettersAndDigits: Boolean
    get() = containsLatinLetter && containsDigit

val String.isIntegerNumber: Boolean
    get() = toIntOrNull() != null

val String.toDecimalNumber: Boolean
    get() = toDoubleOrNull() != null

val String.creditCardFormatted: String
    get() {
        val preparedString = replace(" ", "").trim()
        val result = StringBuilder()
        for (i in preparedString.indices) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ")
            }
            result.append(preparedString[i])
        }
        return result.toString()
    }

val String.asColor: Int?
    get() = try {
        Color.parseColor(this)
    } catch (e: java.lang.IllegalArgumentException) {
        null
    }


fun String.asHtml(): Spanned {
    if (!isNullOrEmpty()) {
        if (contains("\n"))
            return HtmlCompat
                .fromHtml(replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    return HtmlCompat
        .fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}