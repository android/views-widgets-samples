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
import android.util.AttributeSet;
import android.view.View;

/**
 * This view displays two objects sliding right and down while the animation runs.
 * The setFraction() method is called by the animation, which causes the view to
 * invalidate and draw the objects in their new positions.
 */
public class TimingVisualizer extends View {

    float mFraction = 0;
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static float RADIUS = 30;
    private static float mBallTop, mBallBottom, mBallLeft, mBallRight, mBallWidth, mBallHeight;

    public TimingVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setFraction(float fraction) {
        mFraction = fraction;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBallTop = RADIUS;
        mBallLeft = RADIUS;
        mBallRight = getWidth() - RADIUS;
        mBallBottom = getHeight() - RADIUS;
        mBallWidth = mBallRight - mBallLeft;
        mBallHeight = mBallBottom - mBallTop;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        // Draw everything smaller to account for drawing the curve and animated object
        // outside the 1x1 grid
        canvas.translate(width * (1 - CurveVisualizer.SCALE_FACTOR) / 2,
                height * (1 - CurveVisualizer.SCALE_FACTOR) / 2);
        canvas.scale(CurveVisualizer.SCALE_FACTOR, CurveVisualizer.SCALE_FACTOR);

        canvas.drawCircle(mBallLeft, mBallTop + mFraction * mBallHeight, RADIUS, mPaint);
        canvas.drawCircle(mBallLeft + mFraction * mBallWidth, mBallTop, RADIUS, mPaint);
    }
}
