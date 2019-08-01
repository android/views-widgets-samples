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

/**
 * This performs a simple linear interpolation in multiple dimensions
 */
public class LinearInterpolator extends Interpolator {

  private static final String TAG = "LinearInterpolator";
  private double[] mT;
  private double[][] mY;

  public LinearInterpolator(double[] time, double[][] y) {
    final int N = time.length;
    final int dim = y[0].length;
    mT = time;
    mY = y;
  }

  public void getPos(double t, double[] v) {
    final int n = mT.length;
    final int dim = mY[0].length;
    if (t <= mT[0]) {
      for (int j = 0; j < dim; j++) {
        v[j] = mY[0][j];
      }
      return;
    }
    if (t >= mT[n - 1]) {
      for (int j = 0; j < dim; j++) {
        v[j] = mY[n - 1][j];
      }
      return;
    }

    for (int i = 0; i < n - 1; i++) {
      if (t == mT[i]) {
        for (int j = 0; j < dim; j++) {
          v[j] = mY[i][j];
        }
      }
      if (t < mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        for (int j = 0; j < dim; j++) {
          double y1 = mY[i][j];
          double y2 = mY[i + 1][j];

          v[j] = y1 * (1 - x) + y2 * x;
        }
        return;
      }
    }
  }

  public void getPos(double t, float[] v) {
    final int n = mT.length;
    final int dim = mY[0].length;
    if (t <= mT[0]) {
      for (int j = 0; j < dim; j++) {
        v[j] = (float) mY[0][j];
      }
      return;
    }
    if (t >= mT[n - 1]) {
      for (int j = 0; j < dim; j++) {
        v[j] = (float) mY[n - 1][j];
      }
      return;
    }

    for (int i = 0; i < n - 1; i++) {
      if (t == mT[i]) {
        for (int j = 0; j < dim; j++) {
          v[j] = (float) mY[i][j];
        }
      }
      if (t < mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        for (int j = 0; j < dim; j++) {
          double y1 = mY[i][j];
          double y2 = mY[i + 1][j];

          v[j] = (float) (y1 * (1 - x) + y2 * x);
        }
        return;
      }
    }
  }

  public double getPos(double t, int j) {
    final int n = mT.length;
    if (t <= mT[0]) {
      return mY[0][j];
    }
    if (t >= mT[n - 1]) {
      return mY[n - 1][j];
    }

    for (int i = 0; i < n - 1; i++) {
      if (t == mT[i]) {
        return mY[i][j];
      }
      if (t < mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        double y1 = mY[i][j];
        double y2 = mY[i + 1][j];
        return (y1 * (1 - x) + y2 * x);

      }
    }
    return 0; // should never reach here
  }

  public void getSlope(double t, double[] v) {
    final int n = mT.length;
    int dim = mY[0].length;
    if (t <= mT[0]) {
      t = mT[0];
    } else if (t >= mT[n - 1]) {
      t = mT[n - 1];
    }

    for (int i = 0; i < n - 1; i++) {
      if (t <= mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        for (int j = 0; j < dim; j++) {
          double y1 = mY[i][j];
          double y2 = mY[i + 1][j];

          v[j] = (y2 - y1) / h;
        }
        break;
      }
    }
    return;
  }

  public double getSlope(double t, int j) {
    final int n = mT.length;

    if (t < mT[0]) {
      t = mT[0];
    } else if (t >= mT[n - 1]) {
      t = mT[n - 1];
    }
    for (int i = 0; i < n - 1; i++) {
      if (t <= mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        double y1 = mY[i][j];
        double y2 = mY[i + 1][j];
        return (y2 - y1) / h;
      }
    }
    return 0; // should never reach here
  }

  @Override
  double[] getTimePoints() {
    return mT;
  }

}
