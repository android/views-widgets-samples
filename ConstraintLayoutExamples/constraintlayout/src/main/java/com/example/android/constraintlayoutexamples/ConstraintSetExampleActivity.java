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

package com.example.android.constraintlayoutexamples;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

/**
 * An example activity that show cases the use of {@link ConstraintSet}.
 * It starts out with a single ConstraintLayout which is then transitioned to
 * a different ConstraintLayout using {@link ConstraintSet#applyTo(ConstraintLayout)}.
 */
public class ConstraintSetExampleActivity extends AppCompatActivity {

    private static final String SHOW_BIG_IMAGE = "showBigImage";

    /**
     * Whether to show an enlarged image
     */
    private boolean mShowBigImage = false;
    /**
     * The ConstraintLayout that any changes are applied to.
     */
    private ConstraintLayout mRootLayout;
    /**
     * The ConstraintSet to use for the normal initial state
     */
    private ConstraintSet mConstraintSetNormal = new ConstraintSet();
    /**
     * ConstraintSet to be applied on the normal ConstraintLayout to make the Image bigger.
     */
    private ConstraintSet mConstraintSetBig = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraintset_example_main);

        mRootLayout = (ConstraintLayout) findViewById(R.id.activity_constraintset_example);
        // Note that this can also be achieved by calling
        // `mConstraintSetNormal.load(this, R.layout.constraintset_example_main);`
        // Since we already have an inflated ConstraintLayout in `mRootLayout`, clone() is
        // faster and considered the best practice.
        mConstraintSetNormal.clone(mRootLayout);
        // Load the constraints from the layout where ImageView is enlarged.
        mConstraintSetBig.load(this, R.layout.constraintset_example_big);

        if (savedInstanceState != null) {
            boolean previous = savedInstanceState.getBoolean(SHOW_BIG_IMAGE);
            if (previous != mShowBigImage) {
                mShowBigImage = previous;
                applyConfig();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_BIG_IMAGE, mShowBigImage);
    }

    // Method called when the ImageView within R.layout.constraintset_example_main
    // is clicked.
    public void toggleMode(View v) {
        TransitionManager.beginDelayedTransition(mRootLayout);
        mShowBigImage = !mShowBigImage;
        applyConfig();
    }

    private void applyConfig() {
        if (mShowBigImage) {
            mConstraintSetBig.applyTo(mRootLayout);
        } else {
            mConstraintSetNormal.applyTo(mRootLayout);
        }
    }
}
