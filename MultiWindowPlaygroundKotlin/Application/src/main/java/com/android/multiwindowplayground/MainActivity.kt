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

package com.android.multiwindowplayground

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.multiwindowplayground.activities.AdjacentActivity
import com.android.multiwindowplayground.activities.BasicActivity
import com.android.multiwindowplayground.activities.CustomConfigurationChangeActivity
import com.android.multiwindowplayground.activities.LOG_TAG
import com.android.multiwindowplayground.activities.LaunchBoundsActivity
import com.android.multiwindowplayground.activities.LoggingActivity
import com.android.multiwindowplayground.activities.MinimumSizeActivity
import com.android.multiwindowplayground.activities.UnresizableActivity

class MainActivity : LoggingActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display an additional message if the app is not in multiwindow mode.
        findViewById<View>(R.id.warning_multiwindow_disabled).visibility =
                if (!isInMultiWindowMode) View.VISIBLE else View.GONE
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartUnresizableClick(view: View) {
        Log.d(LOG_TAG, "** starting UnresizableActivity")

        /*
         * This activity is marked as 'unresizable' in the AndroidManifest. We need to specify the
         * FLAG_ACTIVITY_NEW_TASK flag here to launch it into a new task stack, otherwise the
         * properties from the root activity would have been inherited (which was here marked as
         * resizable by default).
        */
        val intent = Intent(this, UnresizableActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartMinimumSizeActivity(view: View) {
        Log.d(LOG_TAG, "** starting MinimumSizeActivity")
        startActivity(Intent(this, MinimumSizeActivity::class.java))
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartAdjacentActivity(view: View) {
        Log.d(LOG_TAG, "** starting AdjacentActivity")

        /*
         * Start this activity adjacent to the focused activity (ie. this activity) if possible.
         * Note that this flag is just a hint to the system and may be ignored. For example,
         * if the activity is launched within the same task, it will be launched on top of the
         * previous activity that started the Intent. That's why the Intent.FLAG_ACTIVITY_NEW_TASK
         * flag is specified here in the intent - this will start the activity in a new task.
         */
        val intent = Intent(this, AdjacentActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartLaunchBoundsActivity(view: View) {
        Log.d(LOG_TAG, "** starting LaunchBoundsActivity")

        // Define the bounds in which the Activity will be launched into.
        val bounds = Rect(500, 300, 100, 0)

        // Set the bounds as an activity option.
        val options = ActivityOptions.makeBasic().apply {
            launchBounds = bounds
        }

        // Start the LaunchBoundsActivity with the specified options
        val intent = Intent(this, LaunchBoundsActivity::class.java)
        startActivity(intent, options.toBundle())
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartBasicActivity(view: View) {
        Log.d(LOG_TAG, "** starting BasicActivity")

        // Start an Activity with the default options in the 'singleTask' launch mode as defined in
        // the AndroidManifest.xml.
        startActivity(Intent(this, BasicActivity::class.java))
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartCustomConfigurationActivity(view: View) {
        Log.d(LOG_TAG, "** starting CustomConfigurationChangeActivity")

        // Start an Activity that handles all configuration changes itself.
        startActivity(Intent(this, CustomConfigurationChangeActivity::class.java))
    }

}
