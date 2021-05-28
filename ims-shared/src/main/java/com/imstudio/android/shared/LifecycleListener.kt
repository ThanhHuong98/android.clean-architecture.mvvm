package com.imstudio.android.shared

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/** @Use FragmentLifecycleCallbacks offer a wide range of callbacks that cover the whole lifecycle of a Fragment,
 *  but you can just override the methods you need in your case.
 *  @Usage
 *  val callbacks = LifecycleListener()
 *  supportFragmentManager.registerFragmentLifecycleCallbacks(callbacks, true)
 *
 *  */
open class LifecycleListener : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, b: Bundle?) {

    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, b: Bundle?) {

    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {

    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {

    }
}