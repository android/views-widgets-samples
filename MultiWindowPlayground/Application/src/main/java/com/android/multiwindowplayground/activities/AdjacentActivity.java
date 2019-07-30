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

import android.os.Bundle;
import android.view.View;

/**
 * This Activity is to be launched adjacent to another Activity using the {@link
 * android.content.Intent#FLAG_ACTIVITY_LAUNCH_ADJACENT}.
 *
 * @see com.android.multiwindowplayground.MainActivity#onStartAdjacentActivity(View)
 */
public class AdjacentActivity extends LoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        setBackgroundColor(R.color.teal);
        setDescription(R.string.activity_adjacent_description);
    }

}
