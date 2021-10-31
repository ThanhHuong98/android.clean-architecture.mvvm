package com.nphau.android.shared.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.nphau.android.shared.screens.BindingDSL

abstract class VBBottomSheetDialogFragment<VB : ViewBinding> : IBindingFragment<VB>,
    RoundedBottomSheetDialogFragment() {

    private var _binding: VB? = null

    /**
     * Binding variable to be used for accessing views.
     * @return ViewBinding
     * */
    @BindingDSL
    protected val binding: VB
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(context)
        return requireNotNull(_binding).root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSyncViews(savedInstanceState)
        onSyncEvents()
        onSyncData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
