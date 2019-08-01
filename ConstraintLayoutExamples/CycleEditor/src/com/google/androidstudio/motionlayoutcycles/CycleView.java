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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * A simple graphical interface to KeyCycles
 */
public class CycleView extends JPanel {

  CycleSetModel.Cycle myCycle;
  private static final Color COLOR_CLEAR = new Color(100, 150, 250, 0);
  private static final Color COLOR_BLUE = new Color(100, 150, 250, 85);
  private static final Color SELECTED_COLOR = new Color(100, 150, 250);
  private static final Color REGION_COLOR = new Color(0, 0, 0, 20);

  Color mBackground = new Color(35, 77, 110);
  int ins_top = 30;
  int ins_left = 30;
  int ins_botom = 30;
  int ins_right = 30;
  boolean draw_axis = true;
  boolean draw_grid = true;
  boolean simple_background = true;
  float[][] xPoints = new float[0][];
  float[][] yPoints = new float[0][];
  Color[] pointColor = new Color[0];
  public static final int RANGE_MODE = 21;

  int mTextGap = 2;
  int fcount = 0;
  Color drawing = new Color(0xAAAAAA);
  Color mGridColor = new Color(0x224422);
  Stroke stroke = new BasicStroke(4f,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND);
  Vector<DrawItem> p = new Vector<DrawItem>();
  private int[] pointMode = new int[0];
  public float actual_miny;
  public float actual_maxx;
  public float actual_maxy;
  public float actual_minx;
  private float last_minx;
  private float mTickY;
  private float mTickX;
  private float minx;
  private float last_maxx;
  private float maxx;
  private float maxy;
  private float last_miny;
  private float last_maxy;
  private float miny;
  private int selected_node;
  private int selected_graph;
  Point2D last_click = new Point2D.Double();

  void waveGen(int base) {
    float fb = base / 100f;
    float sc = ((base) % 10000) / 10000f + 1;
    float[] xPoints = new float[1024];
    float[] yPoints = new float[xPoints.length];
    for (int i = 0; i < xPoints.length; i++) {
      float x = (float) ((i + fb) * 10 * Math.PI / xPoints.length);
      xPoints[i] = x;
      yPoints[i] = (float) Math.sin(x) * sc;
    }
    addGraph(0, xPoints, yPoints, Color.WHITE, 0);
  }

  public CycleView(CycleSetModel.Cycle cycle) {
    myCycle = cycle;
    p.addElement(mGrid);
    p.addElement(mAxis);
    p.addElement(mAxisVLabel);
    p.addElement(mAxisHLabel);

    p.addElement(mDrawGraph);
    p.addElement(drawDot);
    waveGen(0);
    calcRange();
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        screenToGraph(e.getPoint(), last_click);
      }
    });

  }

  //
  public void addGraph(int n, double[] x, double[] y, Color c, int mode) {
    float[] xf = new float[x.length];
    float[] yf = new float[y.length];
    for (int i = 0; i < yf.length; i++) {
      xf[i] = (float) x[i];
      yf[i] = (float) y[i];
    }
    addGraph(n, xf, yf, c, mode);
    calcRange();
    repaint();
  }

  public void addGraph(int n, float[] x, double[] y, Color c, int mode) {
    float[] yf = new float[y.length];
    for (int i = 0; i < yf.length; i++) {
      yf[i] = (float) y[i];
    }
    addGraph(n, x, yf, c, mode);
    calcRange();
    repaint();
  }

  public void addGraph(int n, double[] x, float[] y, Color c, int mode) {
    float[] xf = new float[x.length];
    for (int i = 0; i < x.length; i++) {
      xf[i] = (float) x[i];
    }
    addGraph(n, xf, y, c, mode);
    calcRange();
    repaint();
  }

  //
  //
  public void addGraph(int n, double[][] p, Color c, int mode) {
    float[] xf = new float[p.length];
    float[] yf = new float[p.length];
    for (int i = 0; i < yf.length; i++) {
      xf[i] = (float) p[i][0];
      yf[i] = (float) p[i][1];
    }
    addGraph(n, xf, yf, c, mode);
    calcRange();
    repaint();
  }

  public void addGraph(int n, float[] x, float[] y, Color c, int mode) {
    if (xPoints.length <= n) {
      float[][] yp = new float[n + 1][];
      float[][] xp = new float[n + 1][];
      Color[] ncol = new Color[n + 1];
      int[] m = new int[n + 1];
      for (int i = 0; i < xPoints.length; i++) {
        xp[i] = xPoints[i];
        yp[i] = yPoints[i];
        ncol[i] = pointColor[i];
        m[i] = pointMode[i];
      }
      pointColor = ncol;
      xPoints = xp;
      yPoints = yp;
      pointMode = m;
    }
    xPoints[n] = x;
    yPoints[n] = y;
    pointColor[n] = c;

    pointMode[n] = mode;
    calcRange();
    repaint();
  }

  public void addRange(int n, float[] x, float[] y1, float[] y2, Color c) {
    if (xPoints.length <= n) {
      float[][] yp = new float[n + 2][];
      float[][] xp = new float[n + 2][];
      Color[] ncol = new Color[n + 2];
      int[] m = new int[n + 2];
      for (int i = 0; i < xPoints.length; i++) {
        xp[i] = xPoints[i];
        yp[i] = yPoints[i];
        ncol[i] = pointColor[i];
        m[i] = pointMode[i];
      }
      pointColor = ncol;
      xPoints = xp;
      yPoints = yp;
      pointMode = m;
    }
    xPoints[n] = x;
    yPoints[n] = y1;
    pointColor[n] = c;
    pointMode[n] = -1;

    xPoints[n + 1] = x;
    yPoints[n + 1] = y2;
    pointColor[n + 1] = c;
    pointMode[n + 1] = RANGE_MODE;
    calcRange();
    repaint();
  }

  //
  public void calcRange() {
    actual_minx = Float.MAX_VALUE;
    actual_miny = Float.MAX_VALUE;
    actual_maxx = -Float.MAX_VALUE;
    actual_maxy = -Float.MAX_VALUE;
    for (int g = 0; g < xPoints.length; g++) {
      if (xPoints[g] == null | yPoints[g] == null) {
        continue;
      }
      for (int i = 0; i < xPoints[g].length; i++) {
        float x = xPoints[g][i];
        float y = yPoints[g][i];
        actual_minx = Math.min(actual_minx, x);
        actual_miny = Math.min(actual_miny, y);
        actual_maxx = Math.max(actual_maxx, x);
        actual_maxy = Math.max(actual_maxy, y);
      }
    }
  }

  //
  void calcRangeTicks() {
    double dx = actual_maxx - actual_minx;
    double dy = actual_maxy - actual_miny;
    int sw = getWidth();
    int sh = getHeight();

    double border = 1.09345;

    if (Math.abs(last_minx - actual_minx)
        + Math.abs(last_maxx - actual_maxx) > 0.1 * (actual_maxx - actual_minx)) {
      mTickX = (float) calcTick(sw, dx);
      dx = mTickX * Math.ceil(border * dx / mTickX);
      double tx = (actual_minx + actual_maxx - dx) / 2;
      tx = mTickX * Math.floor(tx / mTickX);
      minx = (float) tx;
      tx = (actual_minx + actual_maxx + dx) / 2;
      tx = mTickX * Math.ceil(tx / mTickX);
      maxx = (float) tx;

      last_minx = actual_minx;
      last_maxx = actual_maxx;

    }
    if (Math.abs(last_miny - actual_miny)
        + Math.abs(last_maxy - actual_maxy) > 0.1 * (actual_maxy - actual_miny)) {
      mTickY = (float) calcTick(sh, dy);
      dy = mTickY * Math.ceil(border * dy / mTickY);
      double ty = (actual_miny + actual_maxy - dy) / 2;
      ty = mTickY * Math.floor(ty / mTickY);
      miny = (float) ty;
      ty = (actual_miny + actual_maxy + dy) / 2;
      ty = mTickY * Math.ceil(ty / mTickY);
      maxy = (float) ty;

      last_miny = actual_miny;
      last_maxy = actual_maxy;
    }
  }

  //
  static public double calcTick(int scr, double range) {
    int aprox_x_ticks = scr / 100;
    int type = 1;
    double best = Math.log10(range / (aprox_x_ticks));
    double n = Math.log10(range / (aprox_x_ticks * 2));
    if (frac(n) < frac(best)) {
      best = n;
      type = 2;
    }
    n = Math.log10(range / (aprox_x_ticks * 5));
    if (frac(n) < frac(best)) {
      best = n;
      type = 5;
    }
    return type * Math.pow(10, Math.floor(best));
  }

  static double frac(double x) {
    return x - Math.floor(x);
  }

  interface DrawItem {

    public void paint(Graphics2D g, int w, int h);
  }

  @Override
  protected void paintComponent(Graphics g) {
    fcount++;
    calcRangeTicks();
    int w = getWidth();
    int h = getHeight();
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    if (simple_background) {
      g.setColor(getBackground());
      g.fillRect(0, 0, w, h);
    } else {
      Paint paint = new GradientPaint(0, 0, Color.BLACK, 0, (h * 2) / 3, new Color(
          0x182323), true);
      g2d.setPaint(paint);
      g.fillRect(0, 0, w, h);
    }
    g.setColor(drawing);
    for (DrawItem drawItem : p) {
      drawItem.paint(g2d, w, h);
    }
  }

  //
  public void screenToGraph(Point scr, Point2D graph) {
    int draw_width = getWidth() - ins_left - ins_right;
    int draw_height = getHeight() - ins_top - ins_left;
    float x = minx + (maxx - minx) * ((scr.x - ins_left) / (float) draw_width);
    float y = miny + (maxy - miny) * ((ins_botom + draw_height - scr.y) / (float) draw_height);
    graph.setLocation(x, y);
  }

  //
  DrawItem mAxis = new DrawItem() {
    @Override
    public void paint(Graphics2D g, int w, int h) {
      if (!draw_axis) {
        return;
      }
      g.setColor(drawing);

      g.drawLine(ins_left, ins_top, ins_left, h - ins_botom);
      g.drawLine(ins_left, h - ins_botom, w - ins_right, h - ins_botom);

    }

  };

  DrawItem mGrid = new DrawItem() {
    DecimalFormat df = new DecimalFormat("###.#");

    @Override
    public void paint(Graphics2D g, int w, int h) {
      if (!draw_grid) {
        return;
      }
      g.setColor(mGridColor);
      int draw_width = w - ins_left - ins_right;
      float e = 0.0001f * (maxx - minx);
      FontMetrics fm = g.getFontMetrics();
      int ascent = fm.getAscent();

      for (float i = minx; i <= maxx + e; i += mTickX) {
        int ix = (int) (draw_width * (i - minx) / (maxx - minx) + ins_left);
        g.drawLine(ix, ins_top, ix, h - ins_botom);
        String str = df.format(i);
        int sw = fm.stringWidth(str) / 2;

        g.drawString(str, ix - sw, h - ins_botom + ascent + mTextGap);

      }
      int draw_height = h - ins_top - ins_left;
      e = 0.0001f * (maxy - miny);
      int hightoff = -fm.getHeight() / 2 + ascent;
      for (float i = miny; i <= maxy + e; i += mTickY) {
        int iy = (int) (draw_height * (1 - (i - miny) / (maxy - miny)) + ins_top);
        g.drawLine(ins_left, iy, w - ins_right, iy);
        String str = df.format(i);
        int sw = fm.stringWidth(str);

        g.drawString(str, ins_left - sw - mTextGap, iy + hightoff);
      }
    }

  };

  DrawItem mAxisVLabel = new DrawItem() {

    @Override
    public void paint(Graphics2D g, int w, int h) {

    }

  };
  DrawItem mAxisHLabel = new DrawItem() {

    @Override
    public void paint(Graphics2D g, int w, int h) {

    }

  };
  float dot_x = Float.NaN, dot_y;

  public void setDot(float x, float y) {
    dot_x = x;
    dot_y = y;
    repaint();
  }

  DrawItem drawDot = new DrawItem() {
    int[] lastDotsX = new int[4];
    int[] lastDotsY = new int[lastDotsX.length];
    Color base = Color.YELLOW;
    Color[] dotsColor = new Color[lastDotsX.length];

    {
      Color tmp = base;
      for (int i = dotsColor.length - 1; i >= 0; i--) {
        dotsColor[i] = tmp = tmp.darker();

      }
    }

    @Override
    public void paint(Graphics2D g, int w, int h) {
      if (Float.isNaN(dot_x)) {
        return;
      }
      int draw_width = w - ins_left - ins_right;
      int draw_height = h - ins_top - ins_left;
      float x = draw_width * (dot_x - minx)
          / (maxx - minx) + ins_left;
      float y = draw_height
          * (1 - (dot_y - miny) / (maxy - miny))
          + ins_top;
      int dot_x = (int) x - 3;
      int dot_y = (int) y - 3;

      for (int i = 0; i < lastDotsX.length; i++) {
        g.setColor(dotsColor[i]);
        g.fillRoundRect(lastDotsX[i], lastDotsY[i], 7, 7, 6, 6);
        if (i < lastDotsX.length - 1) {
          lastDotsX[i] = lastDotsX[i + 1];
          lastDotsY[i] = lastDotsY[i + 1];
        } else {
          lastDotsX[i] = dot_x;
          lastDotsY[i] = dot_y;
        }
      }
      g.setColor(base);

      g.fillRoundRect(dot_x, dot_y, 7, 7, 6, 6);

    }

  };

  DrawItem mDrawGraph = new DrawItem() {
    int[] xp = new int[0];
    int[] yp = new int[0];

    @Override
    public void paint(Graphics2D g, int w, int h) {
      g.setColor(drawing);
      if (xPoints.length == 0) {
        return;
      }
      if (xp.length < xPoints[0].length * 2) {
        xp = new int[xPoints[0].length * 2];
        yp = new int[xPoints[0].length * 2];
      }
      int draw_width = w - ins_left - ins_right;
      int draw_height = h - ins_top - ins_left;

      for (int k = 0; k < xPoints.length; k++) {
        if (xPoints[k] == null || yPoints[k] == null) {
          continue;
        }
        for (int i = 0; i < xPoints[k].length; i++) {
          float x = draw_width * (xPoints[k][i] - minx)
              / (maxx - minx) + ins_left;
          float y = draw_height
              * (1 - (yPoints[k][i] - miny) / (maxy - miny))
              + ins_top;
          xp[i] = (int) x;
          yp[i] = (int) y;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(pointColor[k]);
        g.setStroke(stroke);
        switch (pointMode[k]) {
          case -1:
            break;
          case RANGE_MODE:
            for (int i = 0; i < xPoints[k].length; i++) {
              float x = draw_width * (xPoints[k][i] - minx)
                  / (maxx - minx) + ins_left;
              float y1 = draw_height
                  * (1 - (yPoints[k][i] - miny) / (maxy - miny))
                  + ins_top;
              float y2 = draw_height
                  * (1 - (yPoints[k - 1][i] - miny) / (maxy - miny))
                  + ins_top;
              xp[i] = (int) x;
              yp[i] = (int) y1;
              int i2 = xPoints[k].length * 2 - i - 1;
              xp[i2] = (int) x;
              yp[i2] = (int) y2;
            }
            g.fillPolygon(xp, yp, xPoints[k].length * 2);

            if (selected_node >= 0) {
              float x_center = draw_width * (xPoints[selected_graph][selected_node] - minx)
                  / (maxx - minx) + ins_left;
              float x_min = x_center - 0.01f;
              if (selected_node > 0) {
                x_min = draw_width * (xPoints[selected_graph][selected_node - 1] - minx)
                    / (maxx - minx) + ins_left;
              }
              float x_max = x_center;
              if (selected_node < xPoints[selected_graph].length - 1) {
                x_max = draw_width * (xPoints[selected_graph][selected_node + 1] - minx)
                    / (maxx - minx) + ins_left;
              }
              if (x_center >= x_max) {
                float[] spacing = new float[]{0, 0.999f, 1};

                LinearGradientPaint gradientPaint = new LinearGradientPaint(x_min - 1, 1, x_center,
                    1,
                    spacing, new Color[]{COLOR_CLEAR, COLOR_BLUE, COLOR_CLEAR});
                g.setPaint(gradientPaint);
              } else if (x_center <= x_min) {
                float[] spacing = new float[]{0, 0.001f, 1};
                LinearGradientPaint gradientPaint = new LinearGradientPaint(x_center, 1, x_max + 1,
                    1,
                    spacing, new Color[]{COLOR_CLEAR, COLOR_BLUE, COLOR_CLEAR});
                g.setPaint(gradientPaint);
              } else {
                float[] spacing = new float[]{0, (float) (x_center - x_min) / (x_max - x_min), 1};

                LinearGradientPaint gradientPaint = new LinearGradientPaint(x_min, 1, x_max, 1,
                    spacing, new Color[]{COLOR_CLEAR, COLOR_BLUE, COLOR_CLEAR});
                g.setPaint(gradientPaint);

              }

              g.fillPolygon(xp, yp, xPoints[k].length * 2);
              g.drawPolygon(xp, yp, xPoints[k].length * 2);
              g.setColor(pointColor[k]);
              g.drawPolygon(xp, yp, xPoints[k].length * 2);
            }

            break;
          case 2: { // ticks
            final int TICK_SIZE = 2;
            for (int i = 0; i < xPoints[k].length; i++) {
              int pointX = xp[i];
              int pointY = yp[i];
              g.fillRoundRect(pointX - TICK_SIZE, pointY - TICK_SIZE * 4, TICK_SIZE * 2,
                  TICK_SIZE * 8, TICK_SIZE * 2, TICK_SIZE * 8);
            }
            break;
          }
          case 1: {

            final int CIRCLE_SIZE = 10;
            for (int i = 0; i < xPoints[k].length; i++) { // CIRCLE
              int pointX = xp[i];
              int pointY = yp[i];
              if (selected_graph == k && selected_node == i) {
                g.setColor(SELECTED_COLOR);
              } else {
                g.setColor(pointColor[k]);
              }
              g.fillRoundRect(pointX - CIRCLE_SIZE, pointY - CIRCLE_SIZE, CIRCLE_SIZE * 2,
                  CIRCLE_SIZE * 2, CIRCLE_SIZE * 2, CIRCLE_SIZE * 2);
            }
          }
          break;
          default:
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawPolyline(xp, yp, xPoints[k].length);
        }
      }
    }

  };

  public static void save(ActionEvent e, CycleView graph) {
    int w = graph.getWidth();
    int h = graph.getHeight();
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    graph.paint(img.createGraphics());
    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
    int c = chooser.showSaveDialog(graph);
    if (c == JFileChooser.CANCEL_OPTION) {
      System.out.println("cancel");
      return;
    }
    try {
      File f = chooser.getSelectedFile();
      ImageIO.write(img, "png", f);
      System.out.println(f.getAbsolutePath());

      Desktop.getDesktop().open(f.getParentFile());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  static class Oscillator {

    float[] mPeriod = {};
    double[] mPosition = {};
    double[] mArea;

    public static final int SIN_WAVE = 0;
    public static final int SQUARE_WAVE = 1;
    public static final int TRIANGLE_WAVE = 2;
    public static final int SAW_WAVE = 3;
    public static final int REVERSE_SAW_WAVE = 4;
    public static final int COS_WAVE = 5;
    public static final int BOUNCE = 6;

    private int mType;
    double PI2 = Math.PI * 2;

    public Oscillator() {
    }

    public void setType(int type) {
      mType = type;
    }

    public void addPoint(double position, float period) {
      int len = mPeriod.length + 1;

      int j = Arrays.binarySearch(mPosition, position);
      if (j < 0) {
        j = -j - 1;
      }
      mPosition = Arrays.copyOf(mPosition, len);
      mPeriod = Arrays.copyOf(mPeriod, len);
      mArea = new double[len];
      System.arraycopy(mPosition, j, mPosition, j + 1, len - j - 1);
      mPosition[j] = position;
      mPeriod[j] = period;

    }

    public void normalize() {

      double totalArea = 0;
      double totalCount = 0;
      for (int i = 0; i < mPeriod.length; i++) {
        totalCount += mPeriod[i];
      }
      for (int i = 1; i < mPeriod.length; i++) {
        float h = (mPeriod[i - 1] + mPeriod[i]) / 2;
        double w = mPosition[i] - mPosition[i - 1];
        totalArea = totalArea + w * h;
      }
      // scale periods to normalize it
      for (int i = 0; i < mPeriod.length; i++) {
        mPeriod[i] *= totalCount / totalArea;
      }

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
        double m =
            (mPeriod[index] - mPeriod[index - 1]) / (mPosition[index] - mPosition[index - 1]);
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
        case COS_WAVE:
          return Math.cos(PI2 * getP(time));
        case BOUNCE:
          double x = 1 - Math.abs(((getP(time)) * 4) % 4 - 2);
          return 1 - x * x;
      }
    }
  }

  MonotoneSpline mMonotoneSpline;
  Oscillator mOscillator;

  public void setCycle(int n, double[] pos, double[] period, double[] amplitude, double[] offset,
      int selected, int curveType) {
    double[] t = new double[pos.length];
    double[][] v = new double[pos.length][2];

    for (int i = 0; i < pos.length; i++) {
      t[i] = pos[i];
      v[i][0] = amplitude[i];
      v[i][1] = offset[i];
    }
    MonotoneSpline ms = new MonotoneSpline(t, v);
    Oscillator osc = new Oscillator();

    osc.mType = curveType;
    for (int i = 0; i < pos.length; i++) {

      osc.addPoint(pos[i], (float) period[i]);

    }
    osc.normalize();

    mMonotoneSpline = ms;
    mOscillator = osc;
    float[] x = new float[400];
    double[] y1 = new double[x.length];
    float[] yMax = new float[x.length];
    float[] yMin = new float[x.length];
    for (int i = 0; i < x.length; i++) {
      x[i] = (float) (i / (x.length - 1.0f));
      double amp = mMonotoneSpline.getPos(x[i], 0);
      double off = mMonotoneSpline.getPos(x[i], 1);
      y1[i] = mOscillator.getValue(x[i]) * amp + off;
      yMax[i] = (float) (amp + off);
      yMin[i] = (float) (-amp + off);

    }
    n = n * 4;
    addGraph(n, x, y1, Color.BLUE, 0);
    addRange(n + 1, x, yMin, yMax, REGION_COLOR);
    addGraph(n + 3, pos, offset, Color.PINK, 1);
    selected_node = selected;
    selected_graph = n + 3;
  }

  float getComputedValue(float v) {
    if (mMonotoneSpline == null) {
      return 0;
    }
    double amp = mMonotoneSpline.getPos(v, 0);
    double off = mMonotoneSpline.getPos(v, 1);
    return (float) (mOscillator.getValue(v) * amp + off);
  }

  enum Prop {
    PATH_ROTATE,
    ALPHA,
    ELEVATION,
    ROTATION,
    ROTATION_X,
    ROTATION_Y,
    SCALE_X,
    SCALE_Y,
    TRANSLATION_X,
    TRANSLATION_Y,
    TRANSLATION_Z,
    PROGRESS
  }

  static class MainAttribute {

    static String[] Names = {
        "motion:transitionPathRotate",
        "android:alpha",
        "android:elevation",
        "android:rotation",
        "android:rotationX",
        "android:rotationY",
        "android:scaleX",
        "android:scaleY",
        "android:translationX",
        "android:translationY",
        "android:translationZ",
        "motion:progress",
    };
    static String[] ShortNames = {
        "PathRotate",
        "alpha",
        "elevation",
        "rotation",
        "rotationX",
        "rotationY",
        "scaleX",
        "scaleY",
        "translationX",
        "translationY",
        "translationZ",
        "progress",
    };
    static float[][] typicalRange = {
        {-360, 360},
        {0, 1},
        {0, 100},
        {-360, 360},
        {-360, 360},
        {-360, 360},
        {0, 10},
        {0, 10},
        {-200, 200},
        {-200, 200},
        {0, 100},
        {0, 1}
    };
    static boolean[] mapTo100 = {
        false,
        true,
        false,
        false,
        false,
        false,
        true,
        true,
        false,
        false,
        false,
        true,
    };
    static boolean[] makeInt = {
        true,
        false,
        true,
        true,
        true,
        true,
        false,
        false,
        true,
        true,
        true,
        false
    };

    static boolean[] isDp = {
        false,
        false,
        true,
        false,
        false,
        false,
        false,
        false,
        true,
        true,
        true,
        false
    };

    public static String process(double amp, int mAttrIndex) {
      if (isDp[mAttrIndex]) {
        return ((int) amp) + "dp";
      }
      DecimalFormat format = new DecimalFormat("###.####");
      if (typicalRange[mAttrIndex][1] == 1.0f) {

      }
      return format.format(amp);

    }

  }

}
