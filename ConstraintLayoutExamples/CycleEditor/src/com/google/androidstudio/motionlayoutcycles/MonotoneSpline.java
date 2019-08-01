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
 * This performs a spline interpolation in multiple dimensions
 */
public class MonotoneSpline extends Interpolator {

  private static final String TAG = "MonotoneSpline";
  private double[] mT;
  private double[][] mY;
  private double[][] mTangent;

  public MonotoneSpline(double[] time, double[][] y) {
    final int N = time.length;
    final int dim = y[0].length;
    double[][] slope = new double[N - 1][dim]; // could optimize this out
    double[][] tangent = new double[N][dim];
    for (int j = 0; j < dim; j++) {
      for (int i = 0; i < N - 1; i++) {
        double dt = time[i + 1] - time[i];
        slope[i][j] = (y[i + 1][j] - y[i][j]) / dt;
        if (i == 0) {
          tangent[i][j] = slope[i][j];
        } else {
          tangent[i][j] = (slope[i - 1][j] + slope[i][j]) * 0.5f;
        }
      }
      tangent[N - 1][j] = slope[N - 2][j];
    }

    for (int i = 0; i < N - 1; i++) {
      for (int j = 0; j < dim; j++) {
        if (slope[i][j] == 0.) {
          tangent[i][j] = 0.;
          tangent[i + 1][j] = 0.;
        } else {
          double a = tangent[i][j] / slope[i][j];
          double b = tangent[i + 1][j] / slope[i][j];
          double h = Math.hypot(a, b);
          if (h > 9.0) {
            double t = 3. / h;
            tangent[i][j] = t * a * slope[i][j];
            tangent[i + 1][j] = t * b * slope[i][j];
          }
        }
      }
    }
    mT = time;
    mY = y;
    mTangent = tangent;
  }

  @Override
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
          double t1 = mTangent[i][j];
          double t2 = mTangent[i + 1][j];
          v[j] = interpolate(h, x, y1, y2, t1, t2);
        }
        return;
      }
    }
  }

  @Override
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
          double t1 = mTangent[i][j];
          double t2 = mTangent[i + 1][j];
          v[j] = (float) interpolate(h, x, y1, y2, t1, t2);
        }
        return;
      }
    }
  }

  @Override
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
        double t1 = mTangent[i][j];
        double t2 = mTangent[i + 1][j];
        return interpolate(h, x, y1, y2, t1, t2);

      }
    }
    return 0; // should never reach here
  }

  @Override
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
          double t1 = mTangent[i][j];
          double t2 = mTangent[i + 1][j];
          v[j] = diff(h, x, y1, y2, t1, t2) / h;
        }
        break;
      }
    }
    return;
  }

  @Override
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
        double t1 = mTangent[i][j];
        double t2 = mTangent[i + 1][j];
        return diff(h, x, y1, y2, t1, t2) / h;
      }
    }
    return 0; // should never reach here
  }

  public double getLength2D(double t) {
    final int n = mT.length;
    final int dim = mY[0].length;
    if (dim < 2) {
      return 0;
    }
    if (t <= mT[0]) { // before start does not count
      return 0;
    }
    if (t >= mT[n - 1]) {
      t = mT[n - 1]; // clamp to the end
    }
    double sum = 0;

    for (int i = 0; i < n - 1; i++) {
      if (t == mT[i]) {
        double x = mY[i][0];
        double y = mY[i][1];

      }
      if (t >= mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = 1;
        double x1 = mY[i][0];
        double x2 = mY[i + 1][0];
        double tx1 = mTangent[i][0];
        double tx2 = mTangent[i + 1][0];
        double ix = length(h, x, x1, x2, tx1, tx2);
        double y1 = mY[i][1];
        double y2 = mY[i + 1][1];
        double ty1 = mTangent[i][1];
        double ty2 = mTangent[i + 1][1];
        double iy = length(h, x, y1, y2, ty1, ty2);

      } else if (t < mT[i + 1]) {
        double h = mT[i + 1] - mT[i];
        double x = (t - mT[i]) / h;
        double x1 = mY[i][0];
        double x2 = mY[i + 1][0];
        double tx1 = mTangent[i][0];
        double tx2 = mTangent[i + 1][0];
        double ix = length(h, x, x1, x2, tx1, tx2);
        double y1 = mY[i][1];
        double y2 = mY[i + 1][1];
        double ty1 = mTangent[i][1];
        double ty2 = mTangent[i + 1][1];
        double iy = length(h, x, y1, y2, ty1, ty2);

      }
    }
    return 0;
  }

  @Override
  double[] getTimePoints() {
    return mT;
  }

  /**
   * Cubic Hermite spline
   */
  private static double length(double h, double x, double y1, double y2, double t1, double t2) {
    double x2 = x * x;
    double x3 = x2 * x;
    double x4 = x3 * x;
    return
        (-(x4 * y2) / 2) + x3 * y2 + (x4 * y1) / 2 - x3 * y1 + x * y1 + (h * t2 * x4) / 4
            + (h * t1 * x4) / 4 - (h * t2 * x3) / 3 + ((-2) * h * t1 * x3) / 3
            + (h * t1 * x2) / 2;

  }

  /**
   * Cubic Hermite spline
   */
  private static double interpolate(double h, double x, double y1, double y2, double t1,
      double t2) {
    double x2 = x * x;
    double x3 = x2 * x;
    return -2 * x3 * y2 + 3 * x2 * y2 + 2 * x3 * y1 - 3 * x2 * y1 + y1
        + h * t2 * x3 + h * t1 * x3 - h * t2 * x2 - 2 * h * t1 * x2
        + h * t1 * x;
  }

  /**
   * Cubic Hermite spline slope differentiated
   */
  private static double diff(double h, double x, double y1, double y2, double t1, double t2) {
    double x2 = x * x;
    return -6 * x2 * y2 + 6 * x * y2 + 6 * x2 * y1 - 6 * x * y1 + 3 * h * t2 * x2 +
        3 * h * t1 * x2 - 2 * h * t2 * x - 4 * h * t1 * x + h * t1;
  }

  public static void main(String[] a) {
    System.out.println("testing ");
    double[] t = {0, .5, .71};
    double[][] y = {{0, 0}, {100, 40}, {20, 90}, {100, 100}};
    MonotoneSpline m = new MonotoneSpline(t, y);

    dumbLength(m, 0.1);
    dumbLength(m, 0.01);
    dumbLength(m, 0.001);
    dumbLength(m, 0.0001);
    dumbLength(m, 0.00001);

  }

  static void dumbLength(MonotoneSpline m, double delta) {
    double sum = 0;
    double last_x = 0, last_y = 0;
    for (float t = 0; t <= 1; t += delta) {
      double x = m.getPos(t, 0);
      double y = m.getPos(t, 1);
      if (t > 0) {
        sum += Math.hypot(x - last_x, y - last_y);
      }
      last_x = x;
      last_y = y;

    }
    System.out.println("dum sum = " + sum);
  }
}
