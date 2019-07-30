/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.example.android.cardview

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

/**
 * Fragment that demonstrates how to use [CardView].
 */
class CardViewFragment : Fragment() {

    private val TAG = "CardViewFragment"

    // The [CardView] widget.
    @VisibleForTesting lateinit var cardView: CardView

    // SeekBar that changes the cornerRadius attribute for the cardView widget.
    @VisibleForTesting lateinit var radiusSeekBar: SeekBar

    // SeekBar that changes the Elevation attribute for the cardView widget.
    @VisibleForTesting lateinit var elevationSeekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_card_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardView = view.findViewById(R.id.cardview)

        radiusSeekBar = view.findViewById(R.id.cardview_radius_seekbar)
        radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d(TAG, "SeekBar Radius progress: $progress")
                cardView.radius = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit // Do nothing

            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit // Do nothing
        })

        elevationSeekBar = view.findViewById(R.id.cardview_elevation_seekbar)
        elevationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d(TAG, "SeekBar Elevation progress : $progress")
                cardView.elevation = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit // Do nothing

            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit // Do nothing
        })
    }

}
