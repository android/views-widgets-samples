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

package com.android.example.simpletransition;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import static android.widget.RelativeLayout.ABOVE;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {

    CheckBox mStaggerCB;
    Button mFirstButton, mSecondButton, mThirdButton, mFourthButton;
    ViewGroup mSceneRoot;
    TransitionSet mStaggeredTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStaggerCB = (CheckBox) findViewById(R.id.staggerCB);

        mFirstButton = (Button) findViewById(R.id.firstButton);
        mSecondButton = (Button) findViewById(R.id.secondButton);
        mThirdButton = (Button) findViewById(R.id.thirdButton);
        mFourthButton = (Button) findViewById(R.id.fourthButton);

        mSceneRoot = (ViewGroup) findViewById(R.id.activity_main);

        // Create custom transition that 'staggers' the animations by offsetting
        // the individual start times
        mStaggeredTransition = new TransitionSet();
        Transition first = new ChangeBounds();
        Transition second = new ChangeBounds();
        Transition third = new ChangeBounds();
        Transition fourth = new ChangeBounds();

        first.addTarget(mFirstButton);
        second.setStartDelay(50).addTarget(mSecondButton);
        third.setStartDelay(100).addTarget(mThirdButton);
        fourth.setStartDelay(150).addTarget(mFourthButton);

        mStaggeredTransition.addTransition(first).addTransition(second).addTransition(third).
                addTransition(fourth);
    }

    private void alignButtons(boolean left, boolean top) {
        LayoutParams params;

        // Trigger a transition to run after the next layout pass
        if (mStaggerCB.isChecked()) {
            TransitionManager.beginDelayedTransition(mSceneRoot, mStaggeredTransition);
        } else {
            TransitionManager.beginDelayedTransition(mSceneRoot);
        }

        // Change layout parameters of the button stack
        int oldAlignmentLR = left ? ALIGN_PARENT_RIGHT : ALIGN_PARENT_LEFT;
        int newAlignmentLR = left ? ALIGN_PARENT_LEFT : ALIGN_PARENT_RIGHT;
        int oldAlignmentTB = top ? ABOVE : BELOW;
        int newAlignmentTB = top ? BELOW : ABOVE;

        params = (LayoutParams) mFirstButton.getLayoutParams();
        params.addRule(top ? ALIGN_PARENT_BOTTOM : BELOW, 0);
        params.addRule(oldAlignmentLR, 0);
        params.addRule(top ? BELOW : ALIGN_PARENT_BOTTOM, top ? R.id.staggerCB : 1);
        params.addRule(newAlignmentLR);
        mFirstButton.setLayoutParams(params);

        params = (LayoutParams) mSecondButton.getLayoutParams();
        params.addRule(oldAlignmentLR, 0);
        params.addRule(oldAlignmentTB, 0);
        params.addRule(newAlignmentLR);
        params.addRule(newAlignmentTB, R.id.firstButton);
        mSecondButton.setLayoutParams(params);

        params = (LayoutParams) mThirdButton.getLayoutParams();
        params.addRule(oldAlignmentLR, 0);
        params.addRule(oldAlignmentTB, 0);
        params.addRule(newAlignmentLR);
        params.addRule(newAlignmentTB, R.id.secondButton);
        mThirdButton.setLayoutParams(params);

        params = (LayoutParams) mFourthButton.getLayoutParams();
        params.addRule(oldAlignmentLR, 0);
        params.addRule(oldAlignmentTB, 0);
        params.addRule(newAlignmentLR);
        params.addRule(newAlignmentTB, R.id.thirdButton);
        mFourthButton.setLayoutParams(params);
    }

    public void onClick(View view) {
        switch (((Button) view).getText().toString()) {
            case "Top Left":
                alignButtons(true, true);
                break;

            case "Top Right":
                alignButtons(false, true);
                break;

            case "Bottom Left":
                alignButtons(true, false);
                break;

            case "Bottom Right":
                alignButtons(false, false);
                break;
        }
    }
}
