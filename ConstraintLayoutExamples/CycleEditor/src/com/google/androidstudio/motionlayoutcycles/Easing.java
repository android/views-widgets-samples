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
 * This provide standard easing curves
 */
public class Easing {

  static Easing sDefault = new Easing();
  String str = "identity";
  private final static String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
  private final static String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
  private final static String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
  private final static String LINEAR = "cubic(1, 1, 0, 0)";

  public final static String DECELERATE_NAME = "decelerate";
  public final static String ACCELERATE_NAME = "accelerate";
  public final static String STANDARD_NAME = "standard";
  public final static String LINEAR_NAME = "linear";
  public static String[] NAMED_EASING = {STANDARD_NAME, ACCELERATE_NAME, DECELERATE_NAME,
      LINEAR_NAME};

  public static Easing getInterpolator(String configString) {
    if (configString == null) {
      return null;
    }
    if (configString.startsWith("cubic")) {
      return new CubicEasing(configString);
    } else {
      switch (configString) {
        case STANDARD_NAME:
          return new CubicEasing(STANDARD);
        case ACCELERATE_NAME:
          return new CubicEasing(ACCELERATE);
        case DECELERATE_NAME:
          return new CubicEasing(DECELERATE);
        case LINEAR_NAME:
          return new CubicEasing(LINEAR);
        default:
          System.err.println("transitionEasing syntax error syntax:" +
              "transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " +
              Arrays.toString(NAMED_EASING) + " not " + configString);
      }
    }
    return sDefault;
  }

  public double get(double x) {
    return x;
  }

  public String toString() {
    return str;
  }

  public double getDiff(double x) {
    return 1;
  }

  static class CubicEasing extends Easing {

    private static double error = 0.01;
    private static double d_error = 0.0001;
    double x1, y1, x2, y2;

    CubicEasing(String configString) {
      // done this way for efficiency
      str = configString;
      int start = configString.indexOf('(');
      int off1 = configString.indexOf(',', start);
      x1 = Double.parseDouble(configString.substring(start + 1, off1).trim());
      int off2 = configString.indexOf(',', off1 + 1);
      y1 = Double.parseDouble(configString.substring(off1 + 1, off2).trim());
      int off3 = configString.indexOf(',', off2 + 1);
      x2 = Double.parseDouble(configString.substring(off2 + 1, off3).trim());
      int end = configString.indexOf(')', off3 + 1);
      y2 = Double.parseDouble(configString.substring(off3 + 1, end).trim());
    }

    public CubicEasing(double x1, double y1, double x2, double y2) {
      setup(x1, y1, x2, y2);
    }

    void setup(double x1, double y1, double x2, double y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }

    private double getX(double t) {
      double t1 = 1 - t;
      // no need for because start at 0,0 double f0 = (1 - t) * (1 - t) * (1 - t);
      double f1 = 3 * t1 * t1 * t;
      double f2 = 3 * t1 * t * t;
      double f3 = t * t * t;
      return x1 * f1 + x2 * f2 + f3;
    }

    private double getY(double t) {
      double t1 = 1 - t;
      // no need for because start at 0,0 double f0 = (1 - t) * (1 - t) * (1 - t);
      double f1 = 3 * t1 * t1 * t;
      double f2 = 3 * t1 * t * t;
      double f3 = t * t * t;
      return y1 * f1 + y2 * f2 + f3;
    }

    private double getDiffX(double t) {
      double t1 = 1 - t;
      return 3 * t1 * t1 * x1 + 6 * t1 * t * (x2 - x1) + 3 * t * t * (1 - x2);
    }

    private double getDiffY(double t) {
      double t1 = 1 - t;
      return 3 * t1 * t1 * y1 + 6 * t1 * t * (y2 - y1) + 3 * t * t * (1 - y2);
    }

    /**
     * binary search for the region and linear interpolate the answer
     */
    public double getDiff(double x) {
      double t = 0.5;
      double range = 0.5;
      while (range > d_error) {
        double tx = getX(t);
        range *= 0.5;
        if (tx < x) {
          t += range;
        } else {
          t -= range;
        }
      }

      double x1 = getX(t - range);
      double x2 = getX(t + range);
      double y1 = getY(t - range);
      double y2 = getY(t + range);

      return (y2 - y1) / (x2 - x1);
    }

    /**
     * binary search for the region and linear interpolate the answer
     */
    public double get(double x) {
      if (x <= 0.0) {
        return 0;
      }
      if (x >= 1.0) {
        return 1.0;
      }
      double t = 0.5;
      double range = 0.5;
      while (range > error) {
        double tx = getX(t);
        range *= 0.5;
        if (tx < x) {
          t += range;
        } else {
          t -= range;
        }
      }

      double x1 = getX(t - range);
      double x2 = getX(t + range);
      double y1 = getY(t - range);
      double y2 = getY(t + range);

      return (y2 - y1) * (x - x1) / (x2 - x1) + y1;
    }
  }
}
