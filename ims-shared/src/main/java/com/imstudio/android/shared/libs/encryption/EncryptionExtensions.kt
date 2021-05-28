/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.libs.encryption

fun String.toMD5() = EncryptionUtils.encryptMD5ToString(this)

fun String.toSHA1() = EncryptionUtils.encryptSHA1ToString(this)

fun String.toSHA256() = EncryptionUtils.encryptSHA256ToString(this)

fun String.toSHA512() = EncryptionUtils.encryptSHA512ToString(this)

fun String.encryptDES(key: String) = EncryptionUtils.encryptDES(this, key)

fun String.decryptDES(key: String) = EncryptionUtils.decryptDES(this, key)

fun String.encryptAES(key: String) = EncryptionUtils.encryptAES(this, key)

fun String.decryptAES(key: String) = EncryptionUtils.decryptAES(this, key)

fun String.toBASE64(): String = EncryptionUtils.encryptToBase64(this)

fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
