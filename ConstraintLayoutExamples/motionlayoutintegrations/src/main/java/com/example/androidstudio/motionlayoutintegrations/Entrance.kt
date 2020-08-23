package com.example.androidstudio.motionlayoutintegrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityEntranceBinding

class Entrance : AppCompatActivity() {

    private lateinit var binding: ActivityEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
