package com.example.androidstudio.motionlayoutintegrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityCollapsingToolbarBinding
import com.google.android.material.appbar.AppBarLayout

class CollapsingToolbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCollapsingToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listener = AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
            val seekPosition = -verticalOffset / appBar.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }
        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }
}
