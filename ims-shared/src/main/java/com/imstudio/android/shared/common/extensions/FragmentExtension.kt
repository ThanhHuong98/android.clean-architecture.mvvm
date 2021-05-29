/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.imstudio.android.shared.common.delegates.FragmentBundleDelegate
import java.io.Serializable
import kotlin.properties.ReadWriteProperty

/**
 * Extension method to provide hide keyboard for [Fragment].
 */

inline fun <reified T> FragmentManager.findFragmentByTag(): T? {
    return findFragmentByTag(T::class.java.name) as? T
}

fun FragmentManager.removeFragment(fragment: Fragment?): Boolean {
    if (!isDestroyed && fragment != null) {
        inTransaction { remove(fragment) }
        return true
    }
    return false
}

inline fun <reified T : Fragment> FragmentManager.removeFragmentByTag() {
    if (!isDestroyed) {
        findFragmentByTag<T>()?.let { removeFragment(it) }
    }
}

fun <V> Map<String, V>.toBundle(bundle: Bundle = Bundle()): Bundle = bundle.apply {
    forEach {
        put(it.key, it.value)
    }
}

inline fun FragmentManager.inTransaction(
    allowStateLoss: Boolean = true,
    func: FragmentTransaction.() -> FragmentTransaction
) {
    if (!isStateSaved) {
        beginTransaction().func().commit()
    } else if (allowStateLoss) {
        beginTransaction().func().commitAllowingStateLoss()
    }
}

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { replace(frameId, fragment, getTAG()) }
}

fun Fragment.isSafeEnough(): Boolean {
    return context != null && isVisible
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is IBinder -> putBinder(key, value)
        is Bundle -> putBundle(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is FloatArray -> putFloatArray(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        is Short -> putShort(key, value)
        is ShortArray -> putShortArray(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentBundleDelegate()

fun Fragment.makeDial(number: String) {
    requireContext().makeDial(number)
}

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String?>(key)

fun Fragment.setNavigationResult(key: String, value: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun Fragment.clearNavigationResult(key: String) {
    findNavController().currentBackStackEntry?.savedStateHandle?.set(key, null)
}