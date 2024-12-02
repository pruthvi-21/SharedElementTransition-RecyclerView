package com.jw.kmp.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jw.kmp.myapplication.adapter.ImageData.IMAGE_DRAWABLES
import com.jw.kmp.myapplication.fragment.ImageFragment.Companion.newInstance

class ImagePagerAdapter(fragment: Fragment) :
    FragmentStatePagerAdapter(fragment.childFragmentManager) {
    override fun getCount(): Int {
        return IMAGE_DRAWABLES.size
    }

    override fun getItem(position: Int): Fragment {
        return newInstance(IMAGE_DRAWABLES.get(position))
    }
}
