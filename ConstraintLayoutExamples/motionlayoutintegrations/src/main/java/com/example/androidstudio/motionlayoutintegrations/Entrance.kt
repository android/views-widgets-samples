package com.example.androidstudio.motionlayoutintegrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityEntranceBinding

private val SIS_MOTION_LAYOUT_STATE: String = "sis_motion_layout_start"

class Entrance : AppCompatActivity() {

    private lateinit var binding: ActivityEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            binding.motionLayout.transitionState = savedInstanceState.getBundle(SIS_MOTION_LAYOUT_STATE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(SIS_MOTION_LAYOUT_STATE, binding.motionLayout.transitionState)
    }
}
