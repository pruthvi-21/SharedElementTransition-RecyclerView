package com.jw.sharedanim

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jw.sharedanim.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val displayCutout = insets.displayCutout

            var systemBarLeft = systemBars.left
            val systemBarTop = systemBars.top
            var systemBarRight = systemBars.right
            val systemBarBottom = systemBars.bottom

            displayCutout?.let {
                systemBarLeft += it.safeInsetLeft
                systemBarRight += it.safeInsetRight
            }

            binding.root.setPadding(systemBarLeft, systemBarTop, systemBarRight, systemBarBottom)
            insets
        }

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            return
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }

    companion object {
        var currentPosition: Int = 0
        private const val KEY_CURRENT_POSITION =
            "com.google.samples.gridtopager.key.currentPosition"
    }
}
