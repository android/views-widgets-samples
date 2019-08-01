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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

/**
 * This view serves several purposes. The main one is as a visualizer of the timing
 * curve, which it displays on a 1x1 grid. Additionally, a red ball animates along the
 * curve when the animation is run to help visualize how the curve affects the motion
 * of the object. Finally, when the curve is defined by a PathInterpolator, handles for
 * the path's control points are displayed and can be moved around to change the path.
 */
public class CurveVisualizer extends View {

    Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mCurvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mControlPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // This visualizer holds the core interpolator set and used by the app
    Interpolator mInterpolator = new LinearInterpolator();

    public float getCx1() {
        return mCx1;
    }

    public float getCy1() {
        return mCy1;
    }

    public float getCx2() {
        return mCx2;
    }

    public float getCy2() {
        return mCy2;
    }

    // When a PathInterpolator is used, these variables hold the control points
    float mCx1, mCy1, mCx2, mCy2;

    // Variables to indicate whether either control point is being moved by the user
    boolean mMovingC1 = false, mMovingC2 = false;

    // Indicates the type of path used when PathInterpolator is set
    boolean mCubicPath = false;
    boolean mQuadraticPath = false;

    // This callback is used to update the UI with new values for the control points
    // when they are being moved by the user
    ControlPointCallback mPathCallback;

    // The current fraction of the animation. This is set by an animator and
    // affects where the object is drawn to indicate the current animation value
    float mFraction;

    // The size of the control points and animation object
    private static int RADIUS = 30;

    // Scale the grid/curve display to allow the object/curve to draw outside of the 1x1 grid
    static float SCALE_FACTOR = .75f;

    public CurveVisualizer(Context context) {
        super(context);
        mFraction = 0;
        mGridPaint.setStrokeWidth(2f);
        mGridPaint.setColor(Color.GRAY);
        mCurvePaint.setStrokeWidth(5f);
        mCurvePaint.setColor(Color.BLUE);
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setColor(Color.RED);
        mControlPointPaint.setStyle(Paint.Style.FILL);
        mControlPointPaint.setColor(Color.GREEN);
    }

    /**
     * Sets the current interpolator. Note that a PathInterpolator must be set via
     * {@link #setQuadraticInterpolator(float, float, ControlPointCallback)} or
     * {@link #setCubicInterpolator(float, float, float, float, ControlPointCallback)}.
     */
    public void setInterpolator(Interpolator mInterpolator) {
        mQuadraticPath = mCubicPath = false;
        mPathCallback = null;
        this.mInterpolator = mInterpolator;
        invalidate();
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    private void setupPathInterpolator() {
        if (mQuadraticPath) {
            mInterpolator = new PathInterpolator(mCx1, mCy1);
            if (mPathCallback != null) {
                mPathCallback.onControlPoint1Moved(mCx1, mCy1);
            }
        } else if (mCubicPath) {
            mInterpolator = new PathInterpolator(mCx1, mCy1, mCx2, mCy2);
            if (mPathCallback != null) {
                mPathCallback.onControlPoint1Moved(mCx1, mCy1);
                mPathCallback.onControlPoint2Moved(mCx2, mCy2);
            }
        }
        invalidate();
    }

    /**
     * Sets a cubic PathInterpolator, along with a callback object to update the
     * UI when these control points are moved by the user.
     */
    public void setCubicInterpolator(float cx1, float cy1, float cx2, float cy2,
                                     ControlPointCallback callback) {
        mCx1 = cx1;
        mCy1 = cy1;
        mCx2 = cx2;
        mCy2 = cy2;
        mCubicPath = true;
        mQuadraticPath = false;
        mPathCallback = callback;
        setupPathInterpolator();
    }

    /**
     * Sets a quadratic PathInterpolator, along with a callback object to update the
     * UI when the control point is moved by the user.
     */
    public void setQuadraticInterpolator(float cx1, float cy1, ControlPointCallback callback) {
        mCx1 = cx1;
        mCy1 = cy1;
        mQuadraticPath = true;
        mCubicPath = false;
        mPathCallback = callback;
        setupPathInterpolator();
    }

    /**
     * This method is called by the animation to update the current position of the
     * animated object.
     */
    public void setFraction(float fraction) {
        mFraction = fraction;
        invalidate();
    }

    /**
     * This override allows the control points for PathInterpolators to be moved by the
     * user, by dragging them into new positions.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mCubicPath && !mQuadraticPath) {
            return false;
        }
        int action = event.getAction();
        float scaledX = (event.getX() - (getWidth() * (1 - SCALE_FACTOR) / 2)) / SCALE_FACTOR;
        float scaledY = (event.getY() - (getHeight() * (1 - SCALE_FACTOR) / 2)) / SCALE_FACTOR;
        float x = Math.max(Math.min(scaledX / getWidth(), 1), 0);
        float y = Math.max(Math.min((1 - scaledY / getHeight()), 1), 0);
        boolean handled = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (Math.abs(x - mCx1) < .05f && Math.abs(y - mCy1) < .05f) {
                    mMovingC1 = true;
                    handled = true;
                } else if (mCubicPath && Math.abs(x - mCx2) < .05f && Math.abs(y - mCy2) < .05f) {
                    mMovingC2 = true;
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMovingC1) {
                    mCx1 = x;
                    mCy1 = y;
                    if (mPathCallback != null) {
                        mPathCallback.onControlPoint1Moved(x, y);
                    }
                } else if (mMovingC2) {
                    mCx2 = x;
                    mCy2 = y;
                    if (mPathCallback != null) {
                        mPathCallback.onControlPoint2Moved(x, y);
                    }
                }
                setupPathInterpolator();
                handled = true;
                break;
            case MotionEvent.ACTION_UP:
                handled = true;
                mMovingC1 = mMovingC2 = false;
                break;
        }
        if (handled) {
            invalidate();
        }
        return handled;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        // Draw everything smaller to account for drawing the curve and animated object
        // outside the 1x1 grid
        canvas.translate(width * (1 - SCALE_FACTOR) / 2, height * (1 - SCALE_FACTOR) / 2);
        canvas.scale(SCALE_FACTOR, SCALE_FACTOR);

        // Draw horizontal/vertical lines
        for (int i = 0; i <= 100; i += 10) {
            float percentage = (float) i / 100;
            float x = percentage * width;
            float y = percentage * height;
            canvas.drawLine(0, y, width, y, mGridPaint);
            canvas.drawLine(x, 0, x, height, mGridPaint);
        }

        // Draw the interpolator's timing curve
        float lastX = 0;
        float lastY = mInterpolator.getInterpolation(0);
        for (float x = 0; x <= 1f; x += .01f) {
            float y = mInterpolator.getInterpolation(x);
            canvas.drawLine(lastX * width, height - (height * lastY),
                    width * x, height - (height * y), mCurvePaint);
            lastX = x;
            lastY = y;
        }

        // Draw control point(s) for the Path when a PathInterpolator is set
        if (mCubicPath || mQuadraticPath) {
            float cx1Pixels = getWidth() * mCx1;
            float cy1Pixels = getHeight() * (1 - mCy1);
            canvas.drawCircle(cx1Pixels, cy1Pixels, RADIUS * 1.4f, mControlPointPaint);
            if (mCubicPath) {
                float cx2Pixels = getWidth() * mCx2;
                float cy2Pixels = getHeight() * (1 - mCy2);
                canvas.drawCircle(cx2Pixels, cy2Pixels, RADIUS * 1.4f, mControlPointPaint);
            }
        }

        // Draw ball position given the current animation fraction
        float fractionY = mInterpolator.getInterpolation(mFraction);
        float centerX = getWidth() * mFraction;
        float centerY = getHeight() * (1 - fractionY);
        canvas.drawCircle(centerX, centerY, RADIUS, mBallPaint);
    }
}
