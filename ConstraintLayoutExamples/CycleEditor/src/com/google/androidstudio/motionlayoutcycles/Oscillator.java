/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutcycles;

import java.util.Arrays;

/**
 * This produces oscillation patterns
 */
public class Oscillator {

  float[] mPeriod = {20, 2, 4, 8, 200f};
  double[] mPosition = {0.0f, .3f, .5f, .8f, 1};
  double[] mArea;
  public static final int SIN_WAVE = 0;
  public static final int SQUARE_WAVE = 1;
  public static final int TRIANGLE_WAVE = 2;
  public static final int SAW_WAVE = 3;
  public static final int REVERSE_SAW_WAVE = 4;
  private int mType;
  double PI2 = Math.PI * 2;

  public Oscillator(float[] period, double[] pos, int type) {
    mType = type;
    mPeriod = period;
    mPosition = pos;
    mArea = new double[mPosition.length];
    mArea[0] = 0;
    for (int i = 1; i < mPeriod.length; i++) {
      float h = (mPeriod[i - 1] + mPeriod[i]) / 2;
      double w = mPosition[i] - mPosition[i - 1];
      mArea[i] = mArea[i - 1] + w * h;
    }
  }

  public Oscillator() {
    mArea = new double[mPosition.length];
    mArea[0] = 0;
    for (int i = 1; i < mPeriod.length; i++) {
      float h = (mPeriod[i - 1] + mPeriod[i]) / 2;
      double w = mPosition[i] - mPosition[i - 1];
      mArea[i] = mArea[i - 1] + w * h;
    }
  }

  double getP(double time) {
    int index = Arrays.binarySearch(mPosition, time);
    double p = 0;
    if (index > 0) {
      p = 1;
    } else if (index != 0) {
      index = -index - 1;
      double t = time;
      double m = (mPeriod[index] - mPeriod[index - 1]) / (mPosition[index] - mPosition[index - 1]);
      p = mArea[index - 1]
          + (mPeriod[index - 1] - m * mPosition[index - 1]) * (t - mPosition[index - 1])
          + m * (t * t - mPosition[index - 1] * mPosition[index - 1]) / 2;
    }
    return p;
  }

  public double getValue(double time) {

    switch (mType) {
      default:
      case SIN_WAVE:
        return Math.sin(PI2 * getP(time));
      case SQUARE_WAVE:
        return Math.signum(0.5 - getP(time) % 1);
      case TRIANGLE_WAVE:
        return 1 - Math.abs(((getP(time)) * 4 + 1) % 4 - 2);
      case SAW_WAVE:
        return ((getP(time) * 2 + 1) % 2) - 1;
      case REVERSE_SAW_WAVE:
        return (1 - ((getP(time) * 2 + 1) % 2));

    }
  }

  double getSinValue(double time) {
    return Math.sin(PI2 * getP(time));
  }

  double getSquareValue(double time) {
    return Math.signum(0.5 - getP(time) % 1);
  }

  double getTriangle(double time) {
    return 1 - Math.abs(((getP(time)) * 4 + 1) % 4 - 2);
  }

  double getSawValue(double time) {
    return ((getP(time) * 2 + 1) % 2) - 1;
  }

  double getReverseSawValue(double time) {
    return (1 - ((getP(time) * 2 + 1) % 2));
  }

}
