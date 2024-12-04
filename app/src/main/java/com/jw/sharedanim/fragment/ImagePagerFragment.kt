package com.jw.sharedanim.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.jw.sharedanim.MainActivity
import com.jw.sharedanim.adapter.ImagePagerAdapter
import com.jw.sharedanim.databinding.FragmentPagerBinding
import java.util.concurrent.atomic.AtomicBoolean

class ImagePagerFragment : Fragment() {
    private lateinit var binding: FragmentPagerBinding

    val enterTransitionStarted = AtomicBoolean(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.viewPager.adapter = ImagePagerAdapter(this)
        binding.viewPager.currentItem = MainActivity.currentPosition
        binding.viewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                MainActivity.currentPosition = position
            }
        })
    }
}
