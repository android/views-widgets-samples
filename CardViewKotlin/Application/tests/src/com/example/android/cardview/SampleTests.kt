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

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for CardView sample.
 */
@RunWith(AndroidJUnit4::class)
class SampleTests {

    private lateinit var fragment: CardViewFragment

    @Rule @JvmField
    val activityTestRule = ActivityTestRule(CardViewActivity::class.java)

    @Before fun setUp() {
        activityTestRule.activity.fragmentManager.beginTransaction()
        fragment = activityTestRule.activity.fragmentManager
                .findFragmentById(R.id.container) as CardViewFragment
    }

    @Test fun testPreconditions() {
        assertNotNull(activityTestRule.activity)
        assertNotNull(fragment)
        assertNotNull(fragment.radiusSeekBar)
        assertNotNull(fragment.elevationSeekBar)
    }

    @Test fun testRadiusSeekbarChangesRadiusOfCardView() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val radius = 50.0f
            fragment.radiusSeekBar.progress = radius.toInt()
            assertEquals(radius, fragment.cardView.radius)
        }
    }

    @Test fun testElevationSeekbarChangesElevationOfCardView() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val elevation = 40.0f
            fragment.elevationSeekBar.progress = elevation.toInt()
            assertEquals(elevation, fragment.cardView.elevation)
        }
    }

}