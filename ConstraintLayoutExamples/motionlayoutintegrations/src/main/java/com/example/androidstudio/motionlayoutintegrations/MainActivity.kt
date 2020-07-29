package com.example.androidstudio.motionlayoutintegrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.entranceButton.setOnClickListener {
            val intent = Intent(this, Entrance::class.java)
            startActivity(intent)
        }
        binding.toolbarButton.setOnClickListener {
            val intent = Intent(this, CollapsingToolbar::class.java)
            startActivity(intent)
        }
        binding.viewPagerButton.setOnClickListener {
            val intent = Intent(this, ViewPagerCarousel::class.java)
            startActivity(intent)
        }
    }
}
