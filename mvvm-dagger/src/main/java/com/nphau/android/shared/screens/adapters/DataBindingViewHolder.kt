package com.nphau.android.shared.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.nphau.android.shared.common.Comment
import com.nphau.android.shared.common.functional.Provider
import com.nphau.android.shared.common.functional.get
import com.nphau.android.shared.screens.BindingDSL

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
open class DataBindingViewHolder<T>(
    @BindingDSL
    protected open val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry =
        LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    @CallSuper
    @Comment("Need to be placed at the end of function")
    open fun bind(item: T) {
        binding.executePendingBindings()
    }

    open fun context(): Context = itemView.context

    fun onAttached() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onDetached() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    companion object {

        /**
         * inflate with layoutId was provided
         * */
        fun <T : ViewDataBinding> inflate(parent: ViewGroup, provider: Provider<Int>): T =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                provider.get(), parent, false
            )
    }
}
