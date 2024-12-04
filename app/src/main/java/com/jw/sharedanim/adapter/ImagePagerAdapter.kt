package com.jw.sharedanim.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jw.sharedanim.adapter.ImageData.IMAGE_DRAWABLES
import com.jw.sharedanim.fragment.ImageFragment.Companion.newInstance

class ImagePagerAdapter(fragment: Fragment) :
    FragmentStatePagerAdapter(fragment.childFragmentManager) {
    override fun getCount(): Int {
        return IMAGE_DRAWABLES.size
    }

    override fun getItem(position: Int): Fragment {
        return newInstance(IMAGE_DRAWABLES.get(position))
    }
}
