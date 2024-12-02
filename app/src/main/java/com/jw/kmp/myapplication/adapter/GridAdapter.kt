package com.jw.kmp.myapplication.adapter

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jw.kmp.myapplication.MainActivity
import com.jw.kmp.myapplication.R
import com.jw.kmp.myapplication.adapter.GridAdapter.ImageViewHolder
import com.jw.kmp.myapplication.adapter.ImageData.IMAGE_DRAWABLES
import com.jw.kmp.myapplication.databinding.ImageCardBinding
import java.util.concurrent.atomic.AtomicBoolean

class GridAdapter(
    fragment: Fragment,
    navController: NavController,
) : RecyclerView.Adapter<ImageViewHolder>() {

    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)

        fun onItemClicked(view: View, adapterPosition: Int)
    }

    private val viewHolderListener: ViewHolderListener =
        ViewHolderListenerImpl(fragment, navController)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(
            ImageCardBinding.inflate(layoutInflater, parent, false),
            viewHolderListener
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return IMAGE_DRAWABLES.size
    }


    private class ViewHolderListenerImpl(
        private val fragment: Fragment,
        val navController: NavController,
    ) : ViewHolderListener {

        private val enterTransitionStarted = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView?, adapterPosition: Int) {
            if (MainActivity.currentPosition != adapterPosition) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }

        override fun onItemClicked(view: View, adapterPosition: Int) {
            MainActivity.currentPosition = adapterPosition

            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            val rect = Rect(x, y, x + view.width, y + view.height)
            val bundle = Bundle().apply {
                putParcelable("startBounds", rect)
            }

            navController.navigate(R.id.action_grid_fragment_to_pager_fragment, bundle)
        }
    }

    class ImageViewHolder(
        private val binding: ImageCardBinding,
        private val viewHolderListener: ViewHolderListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemContainer.setOnClickListener {
                viewHolderListener.onItemClicked(it, adapterPosition)
            }
        }

        fun onBind() {
            setImage(adapterPosition)
        }

        fun setImage(adapterPosition: Int) {
            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            Glide.with(binding.imageThumbnail.context)
                .load(IMAGE_DRAWABLES[adapterPosition])
                .override(400, 400)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any?,
                        target: Target<Drawable?>, isFirstResource: Boolean,
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(binding.imageThumbnail, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(binding.imageThumbnail, adapterPosition)
                        return false
                    }
                })
                .into(binding.imageThumbnail)
        }
    }

    companion object {
        private const val TAG = "GridAdapter"
    }
}