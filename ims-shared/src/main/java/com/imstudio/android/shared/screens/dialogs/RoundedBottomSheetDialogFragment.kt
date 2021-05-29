package com.imstudio.android.shared.screens.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imstudio.android.shared.R
import com.imstudio.android.shared.common.extensions.longToast
import com.imstudio.android.shared.common.extensions.removeFragmentByTag
import com.imstudio.android.shared.common.functional.tryOrNull
import com.imstudio.android.shared.libs.CommonUtils
import com.imstudio.android.shared.screens.UIBehaviour

open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment(),
    UIBehaviour, IDialogFragment {

    override fun getTheme(): Int = R.style.IMS_BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val bottomSheet = findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
                BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

    override fun dismiss() {
        tryOrNull {
            hideKeyboardIfNeed(activity)
            super.dismissAllowingStateLoss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSyncViews(savedInstanceState)
        onSyncEvents()
        onSyncData()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboardIfNeed(activity)
    }

    override fun showError(message: String?) {
        if (message.isNullOrEmpty()) {
            context?.longToast(R.string.common_error_unknown)
        } else {
            context?.longToast(message)
        }
    }

    override fun onSyncViews(savedInstanceState: Bundle?) {

    }

    override fun onSyncEvents() {

    }

    override fun onSyncData() {

    }

    override fun makeVibrator() {
        CommonUtils.makeVibrator(requireContext())
    }

    override fun doNotCare() {

    }

    override fun showLoading(isShow: Boolean, timeout: Long) {

    }

    inline fun <reified T : Fragment> revival(manager: FragmentManager) {
        manager.removeFragmentByTag<T>()
        super.show(manager, T::class.java.name)
    }

}
