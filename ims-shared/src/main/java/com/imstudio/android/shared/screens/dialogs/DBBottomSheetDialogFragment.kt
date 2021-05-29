package com.imstudio.android.shared.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.imstudio.android.shared.screens.BindingDSL

abstract class DBBottomSheetDialogFragment<DB : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : RoundedBottomSheetDialogFragment() {

    private var _binding: DB? = null

    /**
     * Binding variable to be used for accessing views.
     * @return ViewBinding
     * */
    @BindingDSL
    protected val binding: DB
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )
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
