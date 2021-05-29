package com.imstudio.android.shared.screens.listeners

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class OnItemClickListener(
    context: Context?,
    private val callback: ((view: View, position: Int, isSelected: Boolean) -> Unit?)?
) :
    RecyclerView.OnItemTouchListener {
    private val mGestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        callback?.run {
            if (childView != null && mGestureDetector.onTouchEvent(e))
                callback.invoke(childView, view.getChildAdapterPosition(childView), false)
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}
