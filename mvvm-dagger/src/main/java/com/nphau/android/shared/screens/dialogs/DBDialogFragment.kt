package com.nphau.android.shared.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.nphau.android.shared.screens.BindingDSL

abstract class DBDialogFragment<DB : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    SharedDialogFragment() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}