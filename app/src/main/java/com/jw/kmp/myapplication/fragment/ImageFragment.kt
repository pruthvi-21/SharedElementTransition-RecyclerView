package com.jw.kmp.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jw.kmp.myapplication.R
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

        view.findViewById<View>(R.id.image).transitionName = imageRes.toString()

        Glide.with(this)
            .load(imageRes)
            .into((view.findViewById<View>(R.id.image) as ImageView))
    }

    companion object {
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
