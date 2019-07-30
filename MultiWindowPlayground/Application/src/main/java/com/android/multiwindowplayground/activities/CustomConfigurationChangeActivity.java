/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.multiwindowplayground.activities;

import com.android.multiwindowplayground.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

/**
 * This activity handles configuration changes itself. The list of configuration changes that are
 * supported is defined in its AndroidManifest definition. Each configuration change triggers a
 * call to {@link #onConfigurationChanged(Configuration)}, which is logged in the {@link
 * LoggingActivity}.
 *
 * @see com.android.multiwindowplayground.MainActivity#onStartCustomConfigurationActivity(View)
 */
public class CustomConfigurationChangeActivity extends LoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        setBackgroundColor(R.color.cyan);
        setDescription(R.string.activity_custom_description);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /*
        Note: The implementation in LoggingActivity logs the output o the new configuration.
        This callback is received whenever the configuration is updated, for example when the
        size of this Activity is changed.
         */
    }
}
