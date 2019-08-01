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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ExampleFlyinBounceHelper extends ConstraintHelper {
    protected ConstraintLayout mContainer;

    public ExampleFlyinBounceHelper(Context context) {
        super(context);
    }

    public ExampleFlyinBounceHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleFlyinBounceHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param container
     * @hide
     */
    @Override
    public void updatePreLayout(ConstraintLayout container) {
        if (mContainer!=container) {
            View[] views = getViews(container);
            for (int i = 0; i < mCount; i++) {
                View view = views[i];
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", - 2000, 0).setDuration(1000);
                animator.setInterpolator(new BounceInterpolator());
                animator.start();
            }
        }
        mContainer = container;
    }
}
