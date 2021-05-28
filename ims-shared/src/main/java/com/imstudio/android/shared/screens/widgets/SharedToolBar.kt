package com.imstudio.android.shared.screens.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.imstudio.android.shared.R
import com.imstudio.android.shared.common.extensions.asHtml
import com.imstudio.android.shared.common.extensions.getStyledAttributes
import com.imstudio.android.shared.common.extensions.safeClick
import com.imstudio.android.shared.common.functional.CallBack
import com.imstudio.android.shared.databinding.LayoutToolBarBinding
import com.imstudio.android.shared.databinding.LayoutToolBarLrBinding
import com.imstudio.android.shared.libs.font.FontCache

class SharedToolBar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var tbTextTitle: MaterialTextView? = null
    private var tbImageRight: AppCompatImageView? = null
    private var tbImageLeft: AppCompatImageView? = null

    init {

        context.getStyledAttributes(attributeSet, R.styleable.SharedToolBar) {

            when (getInt(
                R.styleable.SharedToolBar_core_layoutType,
                R.layout.layout_tool_bar
            )) {
                ToolBarType.LEFT -> inflateLeftType()
                ToolBarType.CENTER -> inflateCenterType()
                else -> inflateCenterType()
            }

            initializeTextTitle(this)

            // Background
            if (hasValue(R.styleable.SharedToolBar_core_background)) {
                setBackgroundResource(
                    getResourceId(
                        R.styleable.SharedToolBar_core_background,
                        ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    )
                )
            }

            initializeButton(this)
        }

    }

    private fun inflateCenterType() {
        LayoutToolBarBinding.inflate(LayoutInflater.from(context), this, true)
            .also { binding ->
                tbTextTitle = binding.tbTextTitle
                tbImageLeft = binding.tbImageLeft
                tbImageRight = binding.tbImageRight
            }
    }

    private fun inflateLeftType() {
        LayoutToolBarLrBinding.inflate(LayoutInflater.from(context), this, true)
            .also { binding ->
                tbTextTitle = binding.tbTextTitle
                tbImageLeft = binding.tbImageLeft
                tbImageRight = binding.tbImageRight
            }
    }

    private fun initializeTextTitle(typedArray: TypedArray) {
        // Text title value
        setTitle(typedArray.getString(R.styleable.SharedToolBar_core_title))

        // Text title color
        setTitleColor(typedArray.getColor(R.styleable.SharedToolBar_core_titleColor, Color.WHITE))
        // Text title size
        val textSize = typedArray.getDimension(R.styleable.SharedToolBar_core_titleSize, -1f)
        if (textSize != -1f)
            tbTextTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        // Text title font
        val fontPath = typedArray.getString(R.styleable.SharedToolBar_core_fontFamily)
        FontCache[context, fontPath]?.let { tbTextTitle?.setTypeface(it) }
    }

    private fun initializeButton(typedArray: TypedArray) {
        val left = typedArray.getResourceId(R.styleable.SharedToolBar_core_drawableLeft, -1)
        val right = typedArray.getResourceId(R.styleable.SharedToolBar_core_drawableRight, -1)
        if (left != -1) setDrawableLeft(left)
        if (right != -1) setDrawableRight(right)
    }

    fun setTitle(title: String?) {
        tbTextTitle?.text = title?.asHtml()
        setBinding(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null)
    }

    fun setTitle(title: String, isOnly: Boolean = true) {
        if (isOnly)
            tbTextTitle?.text = title.asHtml()
        else {
            setTitle(title)
        }
    }

    fun setTitleColor(@ColorInt color: Int) {
        tbTextTitle?.setTextColor(color)
    }

    fun onLeftClickListener(listener: CallBack? = null) {
        tbImageLeft?.safeClick { listener?.invoke() }
    }

    fun onRightClickListener(listener: CallBack? = null) {
        tbImageRight?.safeClick { listener?.invoke() }
    }

    fun setDrawableLeft(icon: Int) {
        if (icon == Integer.MIN_VALUE) {
            tbImageLeft?.visibility = View.INVISIBLE
        } else {
            tbImageLeft?.visibility = View.VISIBLE
            tbImageLeft?.setImageResource(icon)
        }
    }

    fun setDrawableRight(icon: Int) {
        if (icon == Integer.MIN_VALUE) {
            tbImageRight?.visibility = View.INVISIBLE
        } else {
            tbImageRight?.visibility = View.VISIBLE
            tbImageRight?.setImageResource(icon)
        }
    }

    fun setBinding(
        drawableLeft: Int, drawableRight: Int,
        leftListener: CallBack?, rightListener: CallBack?
    ) {
        onLeftClickListener(leftListener)
        onRightClickListener(rightListener)
        setDrawableLeft(drawableLeft)
        setDrawableRight(drawableRight)
    }

    fun disableLeft() {
        setDrawableLeft(Integer.MIN_VALUE)
        onLeftClickListener()
    }

    fun disableRight() {
        setDrawableRight(Integer.MIN_VALUE)
        onRightClickListener()
    }
}