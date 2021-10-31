package com.nphau.android.shared.screens.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.nphau.android.shared.R
import com.nphau.android.shared.common.extensions.formatHtml
import com.nphau.android.shared.common.extensions.hideKeyboardIfNeed
import com.nphau.android.shared.common.extensions.longToast
import com.nphau.android.shared.libs.CommonUtils
import com.nphau.android.shared.libs.LocaleUtils
import com.nphau.android.shared.libs.NetworkUtils
import com.nphau.android.shared.screens.UIBehaviour

open class SharedActivity : AppCompatActivity(), UIBehaviour {

    // User for language setting
    override fun attachBaseContext(newBase: Context?) {
        LocaleUtils.self().setLocale(newBase)?.let {
            super.attachBaseContext(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSyncViews(savedInstanceState)
        onSyncEvents()
        onSyncData()
    }

    override fun onSyncViews(savedInstanceState: Bundle?) {

    }

    override fun onSyncEvents() {

    }

    override fun onSyncData() {

    }

    override fun showError(message: String?) {
        var messageNeedToBeShow = message ?: ""
        if (message.isNullOrEmpty()) {
            messageNeedToBeShow = if (NetworkUtils.isNetworkAvailable())
                getString(R.string.common_error_unknown)
            else
                getString(R.string.common_error_connect)
        }
        longToast(messageNeedToBeShow.formatHtml().toString())
    }

    override fun showLoading(isShow: Boolean, timeout: Long) {

    }

    override fun makeVibrator() {
        CommonUtils.makeVibrator(this)
    }

    override fun startActivity(intent: Intent?) {
        try {
            super.startActivity(intent)
            overridePendingTransitionEnter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
            overridePendingTransitionEnter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun doNotCare() {
        // Empty method
    }

    override fun onPause() {
        super.onPause()
        hideKeyboardIfNeed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    private fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.anim_slide_from_right, R.anim.anim_slide_to_left)
    }

    private fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.anim_slide_from_left, R.anim.anim_slide_to_right)
    }

    open fun showLoading() {
        hideKeyboardIfNeed()
        showLoading(true)
    }

    open fun dismissLoading() {
        showLoading(false)
    }

    open fun allowUserDismissKeyboardWhenClickOutSide() = false

    override fun onDestroy() {
        super.onDestroy()
        try {
            dismissLoading()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (allowUserDismissKeyboardWhenClickOutSide())
            hideKeyboardIfNeed()
        return super.dispatchTouchEvent(motionEvent)
    }
}
