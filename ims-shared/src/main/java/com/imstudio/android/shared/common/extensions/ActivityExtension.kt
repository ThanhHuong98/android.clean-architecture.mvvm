/*
 * Created by IMStudio on 5/11/21 10:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/11/21 10:34 AM
 */

package com.imstudio.android.shared.common.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.imstudio.android.shared.common.delegates.ActivityBindingProperty

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideKeyboardIfNeed() {
    if (currentFocus != null)
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
}

// Use for activity data binding
fun <T : ViewDataBinding> activityBinding(@LayoutRes resId: Int) =
    ActivityBindingProperty<T>(
        resId
    )

fun AppCompatActivity.popBackStack() {
    hideKeyboardIfNeed()
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popBackStackInclusive() {
    hideKeyboardIfNeed()
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) {
            add(frameId, fragment, getTAG())
                .addToBackStack(getTAG())
        } else {
            add(frameId, fragment)
        }
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, getTAG()) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment, getTAG()) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack)
            replace(frameId, fragment, getTAG())
                .addToBackStack(getTAG())
        else
            replace(frameId, fragment, getTAG())
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment, frameId: Int,
    addToStack: Boolean, clearBackStack: Boolean
) {
    supportFragmentManager.inTransaction {
        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        if (addToStack) replace(frameId, fragment, getTAG())
            .addToBackStack(getTAG())
        else
            replace(frameId, fragment, getTAG())
    }
}

fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""
    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag =
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
    return fragmentManager.findFragmentByTag(fragmentTag)
}
