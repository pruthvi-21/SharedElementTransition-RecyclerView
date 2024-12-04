package com.jw.sharedanim.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jw.sharedanim.MainActivity
import com.jw.sharedanim.adapter.GridAdapter
import com.jw.sharedanim.databinding.FragmentGridBinding

class GridFragment : Fragment() {
    private lateinit var binding: FragmentGridBinding

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = GridAdapter(this, navController)

        scrollToPosition()
    }

    private fun scrollToPosition() {
        binding.recyclerView.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = binding.recyclerView.layoutManager
                val viewAtPosition =
                    layoutManager!!.findViewByPosition(MainActivity.currentPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    binding.recyclerView.post { layoutManager.scrollToPosition(MainActivity.currentPosition) }
                }
            }
        })
    }
}
