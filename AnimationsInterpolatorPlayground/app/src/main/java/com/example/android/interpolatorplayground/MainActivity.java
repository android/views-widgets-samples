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

package com.example.android.interpolatorplayground;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.lang.reflect.Constructor;

import static android.widget.LinearLayout.LayoutParams;

/**
 * This activity allows the user to visualize the timing curves for
 * most of the standard Interpolator objects. It allows parameterized
 * interpolators to be changed, including manipulating the control
 * points of PathInterpolator to create custom curves.
 */
public class MainActivity extends AppCompatActivity {

    CurveVisualizer mVisualizer;
    TimingVisualizer mTimingVisualizer;
    ObjectAnimator mAnimator = null;
    long mDuration = 300;
    private int mDefaultMargin;
    private LinearLayoutCompat.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDefaultMargin = (int) (8 * metrics.density);

        final LinearLayout paramatersParent = (LinearLayout) findViewById(R.id.linearLayout);
        final LinearLayout gridParent = (LinearLayout) findViewById(R.id.linearLayout2);
        final SeekBar durationSeeker = (SeekBar) findViewById(R.id.durationSeeker);
        final TextView durationLabel = (TextView) findViewById(R.id.durationLabel);
        mTimingVisualizer = (TimingVisualizer) findViewById(R.id.timingVisualizer);
        mAnimator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);

        mVisualizer = new CurveVisualizer(this);
        gridParent.addView(mVisualizer);

        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                populateParametersUI(adapterView.getItemAtPosition(pos).toString(),
                        paramatersParent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        durationSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationLabel.setText("Duration " + progress + "ms");
                mDuration = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**
     * Called when the "Run" button is clicked. It cancels any running animation
     * and starts a new one with the values specified in the UI.
     */
    public void runAnimation(View view) {
        mAnimator.cancel();
        mAnimator.setInterpolator(mVisualizer.getInterpolator());
        mAnimator.setDuration(mDuration);
        mAnimator.start();
    }

    /**
     * This method is called to populate the UI according to which interpolator was
     * selected.
     */
    private void populateParametersUI(String interpolatorName, LinearLayout parent) {
        parent.removeAllViews();
        try {
            switch (interpolatorName) {
                case "Quadratic Path":
                    createQuadraticPathInterpolator(parent);
                    break;
                case "Cubic Path":
                    createCubicPathInterpolator(parent);
                    break;
                case "AccelerateDecelerate":
                    mVisualizer.setInterpolator(new AccelerateDecelerateInterpolator());
                    break;
                case "Linear":
                    mVisualizer.setInterpolator(new LinearInterpolator());
                    break;
                case "Bounce":
                    mVisualizer.setInterpolator(new BounceInterpolator());
                    break;
                case "Accelerate":
                    Constructor<AccelerateInterpolator> decelConstructor =
                            AccelerateInterpolator.class.getConstructor(float.class);
                    createParamaterizedInterpolator(parent, decelConstructor, "Factor", 1, 5, 1);
                    break;
                case "Decelerate":
                    Constructor<DecelerateInterpolator> accelConstructor =
                            DecelerateInterpolator.class.getConstructor(float.class);
                    createParamaterizedInterpolator(parent, accelConstructor, "Factor", 1, 5, 1);
                    break;
                case "Overshoot":
                    Constructor<OvershootInterpolator> overshootConstructor =
                            OvershootInterpolator.class.getConstructor(float.class);
                    createParamaterizedInterpolator(parent, overshootConstructor, "Tension", 1, 5, 1);
                    break;
                case "Anticipate":
                    Constructor<AnticipateInterpolator> anticipateConstructor =
                            AnticipateInterpolator.class.getConstructor(float.class);
                    createParamaterizedInterpolator(parent, anticipateConstructor, "Tension", 1, 5, 1);
                    break;
            }
        } catch (NoSuchMethodException e) {
            Log.e("InterpolatorPlayground", "Error constructing interpolator: " + e);
        }
    }

    /**
     * Creates an Interpolator that takes a single parameter in its constructor.
     * The min/max/default parameters determine how the interpolator is initially
     * set up as well as the values used in the SeekBar for changing this value.
     */
    private void createParamaterizedInterpolator(LinearLayout parent,
        final Constructor constructor, final String name,
        final float min, final float max, final float defaultValue) {
        LinearLayout inputContainer = new LinearLayout(this);
        inputContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin);
        inputContainer.setLayoutParams(params);

        final TextView label = new TextView(this);
        params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = .5f;
        label.setLayoutParams(params);
        String formattedValue = String.format(" %.2f", defaultValue);
        label.setText(name + formattedValue);

        final SeekBar seek = new SeekBar(this);
        params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = .5f;
        seek.setLayoutParams(params);
        seek.setMax(100);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float percentage = (float) i / 100;
                float value = min + percentage * (max - min);
                String formattedValue = String.format(" %.2f", value);
                try {
                    mVisualizer.setInterpolator((Interpolator) constructor.newInstance(value));
                } catch (Throwable error) {
                    Log.e("interpolatorPlayground", error.getMessage());
                }
                label.setText(name + formattedValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        inputContainer.addView(label);
        inputContainer.addView(seek);
        parent.addView(inputContainer);

        try {
            mVisualizer.setInterpolator((Interpolator) constructor.newInstance(defaultValue));
        } catch (Throwable error) {
            Log.e("interpolatorPlayground", error.getMessage());
        }
    }

    /**
     * Creates a quadratic PathInterpolator, whose control point values can be changed
     * by the user dragging that handle around in the UI.
     */
    private void createQuadraticPathInterpolator(LinearLayout parent) {
        float controlX = 0.5f, controlY = .2f;

        LinearLayout inputContainer = new LinearLayout(this);
        inputContainer.setOrientation(LinearLayout.VERTICAL);
        final LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin);
        inputContainer.setLayoutParams(params);

        final TextView cx1Label = new TextView(this);
        cx1Label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        cx1Label.setText("ControlX: " + controlX);
        final TextView cy1Label = new TextView(this);
        cy1Label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        cy1Label.setText("ControlY: " + controlY);

        inputContainer.addView(cx1Label);
        inputContainer.addView(cy1Label);
        parent.addView(inputContainer);

        ControlPointCallback callback = new ControlPointCallback() {
            @Override
            void onControlPoint1Moved(float cx1, float cy1) {
                cx1Label.setText("ControlX: " + String.format("%.2f", cx1));
                cy1Label.setText("ControlY: " + String.format("%.2f", cy1));
            }

            @Override
            void onControlPoint2Moved(float cx2, float cy2) {
            }
        };

        mVisualizer.setQuadraticInterpolator(controlX, controlY, callback);
    }

    /**
     * Creates a cubic PathInterpolator, whose control points values can be changed
     * by the user dragging the handles around in the UI.
     */
    private void createCubicPathInterpolator(LinearLayout parent) {
        float cx1 = 0.5f, cy1 = .2f;
        float cx2 = 0.9f, cy2 = .7f;

        LinearLayout inputContainer = new LinearLayout(this);
        inputContainer.setOrientation(LinearLayout.VERTICAL);
        final LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin);
        inputContainer.setLayoutParams(params);

        final TextView cx1Label = createControlPointLabel("ControlX1", cx1);
        final TextView cy1Label = createControlPointLabel("ControlY1", cy1);
        final TextView cx2Label = createControlPointLabel("ControlX2", cx2);
        final TextView cy2Label = createControlPointLabel("ControlY2", cy2);

        inputContainer.addView(cx1Label);
        inputContainer.addView(cy1Label);
        inputContainer.addView(cx2Label);
        inputContainer.addView(cy2Label);
        parent.addView(inputContainer);

        final ControlPointCallback callback = new ControlPointCallback() {
            @Override
            void onControlPoint1Moved(float cx, float cy) {
                cx1Label.setText("ControlX1: " + String.format("%.2f", cx));
                cy1Label.setText("ControlY1: " + String.format("%.2f", cy));
            }

            @Override
            void onControlPoint2Moved(float cx, float cy) {
                cx2Label.setText("ControlX2: " + String.format("%.2f", cx));
                cy2Label.setText("ControlY2: " + String.format("%.2f", cy));
            }
        };

        // Buttons to set control points from standard Material Design interpolators
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttonContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonContainer.addView(createMaterialMotionButton("L out S in",
                0, 0, .2f, 1, .33f, callback));
        buttonContainer.addView(createMaterialMotionButton("F out S in",
                .4f, 0, .2f, 1, .33f, callback));
        buttonContainer.addView(createMaterialMotionButton("F out L in",
                .4f, 0, 1, 1, .33f, callback));
        inputContainer.addView(buttonContainer);

        mVisualizer.setCubicInterpolator(cx1, cy1, cx2, cy2, callback);
    }

    @NonNull
    private TextView createControlPointLabel(String label, float value) {
        final TextView cx1Label = new TextView(this);
        cx1Label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        cx1Label.setText(label + ": " + value);
        return cx1Label;
    }

    private Button createMaterialMotionButton(String label,
            final float cx1, final float cy1, final float cx2, final float cy2,
            float weight, final ControlPointCallback callback) {
        Button button = new Button(this);
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = weight;
        button.setLayoutParams(params);
        button.setText("F out L in");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animate the control points to their new values
                final float oldCx1 = mVisualizer.getCx1();
                final float oldCy1 = mVisualizer.getCy1();
                final float oldCx2 = mVisualizer.getCx2();
                final float oldCy2 = mVisualizer.getCy2();
                ValueAnimator anim = ValueAnimator.ofFloat(0, 1).setDuration(100);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float t = valueAnimator.getAnimatedFraction();
                        mVisualizer.setCubicInterpolator(oldCx1 + t * (cx1 - oldCx1),
                                oldCy1 + t * (cy1 - oldCy1),
                                oldCx2 + t * (cx2 - oldCx2),
                                oldCy2 + t * (cy2 - oldCy2), callback);
                    }
                });
                anim.start();
            }
        });
        return button;
    }

    /**
     * This method is called by the animation to update the position of the animated
     * objects in the curve view as well as the view at the bottom showing sample animations.
     */
    public void setFraction(float fraction) {
        mTimingVisualizer.setFraction(fraction);
        mVisualizer.setFraction(fraction);
    }

}
