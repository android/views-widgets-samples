package com.example.androidstudio.motionlayoutintegrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityEntranceBinding

private val SIS_MOTION_LAYOUT_START_STATE: String = "sis_motion_layout_start"
private val SIS_MOTION_LAYOUT_END_STATE: String = "sis_motion_layout_end"
private val SIS_MOTION_LAYOUT_PROGRESS = "sis_motion_layout_progress"

class Entrance : AppCompatActivity() {

    private lateinit var binding: ActivityEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            binding.motionLayout.setTransition(
                savedInstanceState.getInt(SIS_MOTION_LAYOUT_START_STATE),
                savedInstanceState.getInt(SIS_MOTION_LAYOUT_END_STATE)
            )

            binding.motionLayout.progress = savedInstanceState.getFloat(SIS_MOTION_LAYOUT_PROGRESS)
        }

        // Kick off the first transition, or the restored transition
        binding.motionLayout.transitionToEnd()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SIS_MOTION_LAYOUT_START_STATE, binding.motionLayout.startState)
        outState.putInt(SIS_MOTION_LAYOUT_END_STATE, binding.motionLayout.endState)
        outState.putFloat(SIS_MOTION_LAYOUT_PROGRESS, binding.motionLayout.progress)
    }
}
