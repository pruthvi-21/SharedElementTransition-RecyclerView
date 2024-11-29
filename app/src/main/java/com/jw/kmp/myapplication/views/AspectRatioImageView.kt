package com.jw.kmp.myapplication.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioImageView(
    context: Context,
    attrs: AttributeSet?,
) : AppCompatImageView(context, attrs) {

    private var aspectRatio: Float = 1f

    init {
        // Ensure the scale type is set to center crop by default
//        scaleType = ScaleType.CENTER_CROP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val drawable: Drawable = drawable ?: return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight

        val aspectRatio = imageWidth.toFloat() / imageHeight.toFloat()

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        val newWidth: Int
        val newHeight: Int

        if (imageWidth > imageHeight) {
            newHeight = parentHeight
            newWidth = (newHeight * aspectRatio).toInt()
        } else {
            newWidth = parentWidth
            newHeight = (newWidth / aspectRatio).toInt()
        }

        // Set the new measured dimensions
        setMeasuredDimension(newWidth, newHeight)
    }
}