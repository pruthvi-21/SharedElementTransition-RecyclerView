package com.jw.kmp.myapplication.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jw.kmp.myapplication.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private lateinit var binding: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = arguments
        @DrawableRes val imageRes = arguments!!.getInt(KEY_IMAGE_RES)

        Glide.with(this)
            .load(imageRes)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    handleImageTransition(resource)
                    return false
                }

            })
            .dontAnimate()
            .into(binding.image.imageView)
    }

    private fun handleImageTransition(drawable: Drawable) {
        (parentFragment as? ImagePagerFragment)?.let { parent ->
            if (!parent.enterTransitionStarted.getAndSet(true)) {
                val startBounds = getStartBounds()
                val targetBounds = getTargetBounds(drawable)

                getAnimatorSet(from = startBounds, to = targetBounds).start()
            } else {
                val targetBounds = getTargetBounds(drawable)

                binding.image.apply {
                    x = targetBounds.left.toFloat()
                    y = targetBounds.top.toFloat()

                    layoutParams = layoutParams.apply {
                        width = targetBounds.width()
                        height = targetBounds.height()
                    }
                    requestLayout()
                }
            }
        }
    }

    private fun getAnimatorSet(from: Rect, to: Rect): AnimatorSet {
        val targetView = binding.image

        val xAnimator = createIntAnimator(from.left, to.left) {
            targetView.x = it.toFloat()
        }
        val yAnimator = createIntAnimator(from.top, to.top) {
            targetView.y = it.toFloat()
        }

        val widthAnimator = createIntAnimator(from.width(), to.width()) {
            targetView.layoutParams = targetView.layoutParams.apply { width = it }
        }

        val heightAnimator = createIntAnimator(from.height(), to.height()) {
            targetView.layoutParams = targetView.layoutParams.apply { height = it }
        }

        return AnimatorSet().apply {
            playTogether(
                xAnimator,
                yAnimator,
                widthAnimator,
                heightAnimator
            )
            duration = 200
            interpolator = FastOutSlowInInterpolator()
        }
    }

    private fun createIntAnimator(start: Int, end: Int, onUpdate: (Int) -> Unit): ValueAnimator {
        return ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { onUpdate(it.animatedValue as Int) }
        }
    }

    private fun getStartBounds(): Rect {
        val startBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parentFragment?.arguments?.getParcelable("startBounds", Rect::class.java)
        } else {
            parentFragment?.arguments?.getParcelable("startBounds")
        }!!

        binding.image.layoutParams = binding.image.layoutParams.apply {
            width = startBounds.width()
            height = startBounds.height()
        }

        val parentLocation = IntArray(2)
        binding.container.getLocationOnScreen(parentLocation)

        val relativeX = startBounds.left - parentLocation[0]
        val relativeY = startBounds.top - parentLocation[1]

        return Rect(
            relativeX,
            relativeY,
            relativeX + startBounds.width(),
            relativeY + startBounds.height()
        )
    }

    private fun getTargetBounds(drawable: Drawable): Rect {
        val parent = binding.container

        val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight
        val parentAspectRatio = parent.width.toFloat() / parent.height

        val newWidth: Int
        val newHeight: Int

        if (parentAspectRatio > aspectRatio) {
            newHeight = parent.height
            newWidth = (newHeight * aspectRatio).toInt()
        } else {
            newWidth = parent.width
            newHeight = (newWidth / aspectRatio).toInt()
        }

        val newX = 0.coerceAtLeast(parent.width / 2 - newWidth / 2)
        val newY = 0.coerceAtLeast(parent.height / 2 - newHeight / 2)

        return Rect(
            newX,
            newY,
            newX + newWidth,
            newY + newHeight
        )
    }

    companion object {
        private const val TAG = "ImageFragment"
        private const val KEY_IMAGE_RES = "com.google.samples.gridtopager.key.imageRes"

        @JvmStatic
        fun newInstance(@DrawableRes drawableRes: Int): ImageFragment {
            val fragment = ImageFragment()
            val argument = Bundle()
            argument.putInt(KEY_IMAGE_RES, drawableRes)
            fragment.arguments = argument
            return fragment
        }
    }
}
