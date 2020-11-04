/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample.flowerDetail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclersample.R
import com.example.recyclersample.flowerList.FLOWER_ID

class FlowerDetailActivity : AppCompatActivity() {

    private val flowerDetailViewModel by viewModels<FlowerDetailViewModel> {
        FlowerDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flower_detail_activity)

        var currentFlowerId: Long? = null

        /* Connect variables to UI elements. */
        val flowerName: TextView = findViewById(R.id.flower_detail_name)
        val flowerImage: ImageView = findViewById(R.id.flower_detail_image)
        val flowerDescription: TextView = findViewById(R.id.flower_detail_description)
        val removeFlowerButton: Button = findViewById(R.id.remove_button)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentFlowerId = bundle.getLong(FLOWER_ID)
        }

        /* If currentFlowerId is not null, get corresponding flower and set name, image and
        description */
        currentFlowerId?.let {
            val currentFlower = flowerDetailViewModel.getFlowerForId(it)
            flowerName.text = currentFlower?.name
            if (currentFlower?.image == null) {
                flowerImage.setImageResource(R.drawable.rose)
            } else {
                flowerImage.setImageResource(currentFlower.image)
            }
            flowerDescription.text = currentFlower?.description

            removeFlowerButton.setOnClickListener {
                if (currentFlower != null) {
                    flowerDetailViewModel.removeFlower(currentFlower)
                }
                finish()
            }
        }

    }
}