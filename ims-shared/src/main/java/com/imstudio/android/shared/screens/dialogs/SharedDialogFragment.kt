package com.imstudio.android.shared.screens.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imstudio.android.shared.common.extensions.removeFragmentByTag

open class SharedDialogFragment : DialogFragment(), IDialogFragment {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext(), theme)
        dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
        dialogView?.let { onViewCreated(it, savedInstanceState) }
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside())
        return dialog
    }

    override fun getView() = dialogView

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSyncViews(savedInstanceState)
        onSyncEvents()
        onSyncData()
    }

    open fun onSyncViews(savedInstanceState: Bundle?) {}

    open fun onSyncEvents() {}

    open fun onSyncData() {}

    override fun dismiss() {
        try {
            if (isAdded)
                super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inline fun <reified T : Fragment> revival(manager: FragmentManager) {
        manager.removeFragmentByTag<T>()
        super.show(manager, T::class.java.name)
    }

    protected open fun isCanceledOnTouchOutside(): Boolean = true

}
