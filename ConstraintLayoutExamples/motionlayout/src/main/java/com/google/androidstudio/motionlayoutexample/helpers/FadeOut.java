/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.motion.widget.MotionHelper;

public class FadeOut extends MotionHelper {

    public FadeOut(Context context) {
        super(context);
    }

    public FadeOut(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadeOut(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgress(View view, float progress) {
        view.setAlpha(1f - progress);
    }
}
