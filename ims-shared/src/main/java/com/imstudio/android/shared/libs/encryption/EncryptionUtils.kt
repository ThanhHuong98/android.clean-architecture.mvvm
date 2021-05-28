/*
 * Created by IMStudio on 5/11/21 10:40 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.libs.encryption

import android.util.Base64
import com.imstudio.android.shared.common.functional.tryOrNull
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {


    ///////////////////////////////////////////////////////////////////////////
    // hash encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptMD5ToString(data.toByteArray())
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?, salt: String?): String {
        if (data == null && salt == null) return ""
        if (salt == null) return bytes2HexString(encryptMD5(data!!.toByteArray()))
        return if (data == null) bytes2HexString(encryptMD5(salt.toByteArray())) else bytes2HexString(
            encryptMD5((data + salt).toByteArray())
        )
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: ByteArray): String {
        return bytes2HexString(encryptMD5(data))
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: ByteArray?, salt: ByteArray?): String {
        if (data == null && salt == null) return ""
        if (salt == null) return bytes2HexString(data?.let { encryptMD5(it) })
        if (data == null) return bytes2HexString(encryptMD5(salt))
        val dataSalt = ByteArray(data.size + salt.size)
        System.arraycopy(data, 0, dataSalt, 0, data.size)
        System.arraycopy(salt, 0, dataSalt, data.size, salt.size)
        return bytes2HexString(encryptMD5(dataSalt))
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    fun encryptMD5(data: ByteArray): ByteArray? {
        return hashTemplate(data, "MD5")
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    fun encryptSHA1ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA1ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    fun encryptSHA1ToString(data: ByteArray): String {
        return bytes2HexString(encryptSHA1(data))
    }

    /**
     * Return the bytes of SHA1 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA1 encryption
     */
    fun encryptSHA1(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA1")
    }


    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    fun encryptSHA256ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA256ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    fun encryptSHA256ToString(data: ByteArray): String {
        return bytes2HexString(encryptSHA256(data))
    }

    /**
     * Return the bytes of SHA256 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA256 encryption
     */
    fun encryptSHA256(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA-256")
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    fun encryptSHA512ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA512ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    fun encryptSHA512ToString(data: ByteArray): String {
        return bytes2HexString(encryptSHA512(data))
    }

    /**
     * Return the bytes of SHA512 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA512 encryption
     */
    fun encryptSHA512(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA-512")
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    private fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
        if (data == null || data.isEmpty()) return null
        return tryOrNull {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // hmac encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun encryptHmacMD5ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacMD5ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun encryptHmacMD5ToString(data: ByteArray, key: ByteArray): String {
        return bytes2HexString(encryptHmacMD5(data, key))
    }

    /**
     * Return the bytes of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacMD5 encryption
     */
    fun encryptHmacMD5(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacMD5")
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun encryptHmacSHA1ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacSHA1ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun encryptHmacSHA1ToString(data: ByteArray, key: ByteArray): String {
        return bytes2HexString(encryptHmacSHA1(data, key))
    }

    /**
     * Return the bytes of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA1 encryption
     */
    fun encryptHmacSHA1(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA1")
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun encryptHmacSHA256ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacSHA256ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun encryptHmacSHA256ToString(data: ByteArray, key: ByteArray): String {
        return bytes2HexString(encryptHmacSHA256(data, key))
    }

    /**
     * Return the bytes of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA256 encryption
     */
    fun encryptHmacSHA256(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA256")
    }

    /**
     * Return the bytes of hmac encryption.
     *
     * @param data      The data.
     * @param key       The key.
     * @param algorithm The name of hmac encryption.
     * @return the bytes of hmac encryption
     */
    private fun hmacTemplate(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String
    ): ByteArray? {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return null
        return try {
            val secretKey = SecretKeySpec(key, algorithm)
            val mac = Mac.getInstance(algorithm)
            mac.init(secretKey)
            mac.doFinal(data)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }

    }


    private val HEX_DIGITS =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    private fun bytes2HexString(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val len = bytes.size
        if (len <= 0) return ""
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            ret[j++] = HEX_DIGITS[bytes[i] shr 4 and 0x0f]
            ret[j++] = HEX_DIGITS[bytes[i] and 0x0f]
            i++
        }
        return String(ret)
    }

    private infix fun Byte.and(that: Int): Int = this.toInt().and(that)
    private infix fun Byte.shr(that: Int): Int = this.toInt().shr(that)

    /**
     * AES, key length must be 16
     * @param input
     * @param key key length must be 16
     * @return
     */
    fun encryptAES(input: String, key: String): String? {
        return encryptBySymmetrical("AES", input, key)
    }

    /**
     * AES，key length must be 16
     * @param input
     * @param key key length must be 16
     * @return
     */
    fun decryptAES(input: String, key: String): String? {
        return decryptBySymmetrical("AES", input, key)
    }

    /**
     * DES, key length must be 8
     * @param input
     * @param key key length must be 8
     * @return
     */
    fun encryptDES(input: String, key: String): String? {
        return encryptBySymmetrical("DES", input, key)
    }

    /**
     * DES, key length must be 8
     * @param input
     * @param key key length must be 8
     * @return
     */
    fun decryptDES(input: String, key: String): String? {
        return decryptBySymmetrical("DES", input, key)
    }

    private fun encryptBySymmetrical(name: String, input: String, key: String): String? {
        try {
            val cipher = Cipher.getInstance(name)
            val secretKeySpec = SecretKeySpec(key.toByteArray(), name)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(input.toByteArray())
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun decryptBySymmetrical(name: String, input: String, key: String): String? {
        try {
            val cipher = Cipher.getInstance(name)
            val secretKeySpec = SecretKeySpec(key.toByteArray(), name)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
            return String(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    fun decryptFromBase64(needToDecrypt: String?): String {
        val data = Base64.decode(needToDecrypt, Base64.DEFAULT)
        return String(data, StandardCharsets.UTF_8)
    }

    @JvmStatic
    fun encryptToBase64(needToEncrypt: String): String {
        val data = needToEncrypt.toByteArray(StandardCharsets.UTF_8)
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

}
