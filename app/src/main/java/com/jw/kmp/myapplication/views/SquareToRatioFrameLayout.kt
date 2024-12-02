package com.jw.kmp.myapplication.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

class SquareToRatioFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    val imageView = AspectRatioImageView(context, null)

    init {
        removeAllViews()
        addView(imageView)

        imageView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
        ).apply {
            gravity = Gravity.CENTER
        }
    }

    fun adjustSize(parent: View, drawable: Drawable) {
        val ratio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
        val parentAspectRatio = parent.width.toFloat() / parent.height

        val newWidth: Int
        val newHeight: Int

        if (parentAspectRatio > ratio) {
            newHeight = parent.height
            newWidth = (newHeight * ratio).toInt()
        } else {
            newWidth = parent.width
            newHeight = (newWidth / ratio).toInt()
        }

        layoutParams = layoutParams.apply {
            width = newWidth
            height = newHeight
        }
        requestLayout()
    }
}