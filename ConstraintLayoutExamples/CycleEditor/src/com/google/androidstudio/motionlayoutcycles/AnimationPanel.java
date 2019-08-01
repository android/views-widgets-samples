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

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * This panel simulates and Android view parameter for controlling a button
 */
class AnimationPanel extends JPanel {

  public static final String[] EASING_OPTIONS = Easing.NAMED_EASING;
  private static final boolean RENDERING_STATS = Boolean.parseBoolean(System.getProperty("stats"));
  private float mDuration = 20000; // duration in milliseconds
  private float myAnimationPercent;
  private long myLastTime;
  private static final int BUTTON_WIDTH = 123;
  private static final int BUTTON_HEIGHT = 40;
  private String myTitle = "button";
  private JButton myPlayButton;
  private boolean myIsPlaying = false;
  private Timer myPlayTimer = new Timer(14, e -> step());
  private CycleSetModel myCycleModel;
  private int myAttributeMask;
  private Easing myEasing = Easing.getInterpolator(Easing.STANDARD_NAME);
  private float myEasedPercent;
  static final Stroke DASH_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
      1, new float[]{10, 10}, 0);

  AnimationPanel(CycleSetModel cycleModel, JButton play) {
    super(new FlowLayout(FlowLayout.LEFT));
    setBackground(new Color(0xF8F8F4));
    setBorder(new  LineBorder(new Color(0x888888),1));
    myCycleModel = cycleModel;
    myPlayButton = play;
    myPlayButton.addActionListener(e -> play());
  }

  public void setEasing(String easing) {
    myEasing = Easing.getInterpolator(easing);
  }

  public void setModel(CycleSetModel cycleModel) {
    myCycleModel = cycleModel;
  }

  public void setMode() {
    myAttributeMask = myCycleModel.getAttributeMask();
  }

  public void play() {
    if (myIsPlaying) {
      myCycleModel.setDot(Float.NaN);
      pause();
      myIsPlaying = false;
      return;
    }
    myPlayButton.setText("pause");
    myIsPlaying = true;
    myAttributeMask = myCycleModel.getAttributeMask();
    myPlayTimer.start();
  }

  int call_count = 0;
  int paint_count = 0;
  long last_update;

  public void step() {
    long time = System.currentTimeMillis();
    myAnimationPercent += (time - myLastTime) / mDuration;
    if (myAnimationPercent > 1.0f) {
      myAnimationPercent = 0;
    }
    myEasedPercent = (float) myEasing.get(myAnimationPercent);

    myCycleModel.setDot(myEasedPercent);
    call_count++;
    repaint();
    myLastTime = time;
  }

  public void pause() {
    myPlayButton.setText("play");
    myPlayTimer.stop();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    paint_count++;
    long time = System.currentTimeMillis();
    int w = getWidth();
    int h = getHeight();
    float buttonCX = w / 2.0f;
    float buttonCY = h / 2.0f;
    float startX = buttonCX;
    float startY = buttonCY;
    float endX = buttonCX;
    float endY = buttonCY;
    if (myMoveObject != 0) {
      float dx = movement[myMoveObject][0];
      float dy = movement[myMoveObject][1];
      startX = buttonCX - (dx * w) / 3.0f;
      startY = buttonCY - (dy * h) / 3.0f;
      endX = buttonCX + (dx * w) / 3.0f;
      endY = buttonCY + (dy * h) / 3.0f;
      buttonCX = startX + myEasedPercent * (endX - startX);
      buttonCY = startY + myEasedPercent * (endY - startY);

    }
    Graphics2D g2d = (Graphics2D) g.create();
    AffineTransform at;
    Stroke old = g2d.getStroke();
    g2d.setColor(Color.RED);
    g2d.setStroke(DASH_STROKE);
    g2d.drawLine((int) startX, (int) startY, (int) endX, (int) endY);
    g2d.setStroke(old);
    Color background = Color.LIGHT_GRAY;
    Color border = Color.DARK_GRAY;
    Color text = Color.BLACK;
    if (myAttributeMask != 0) {

      if ((myAttributeMask & (1 << CycleView.Prop.PATH_ROTATE.ordinal())) != 0) {
        at = new AffineTransform();
        at.rotate(Math.toRadians(myCycleModel.getValue(CycleView.Prop.PATH_ROTATE, myEasedPercent)), buttonCX,
            buttonCY);
        g2d.transform(at);
      }
      if ((myAttributeMask & (1 << CycleView.Prop.ALPHA.ordinal())) != 0) {
        int alpha = Math
            .max(0, Math.min(255, (int) (myCycleModel.getValue(CycleView.Prop.ALPHA, myEasedPercent) * 255)));
        background = new Color(background.getRed(), background.getGreen(), background.getBlue(),
            alpha);
        border = new Color(border.getRed(), border.getGreen(), border.getBlue(), alpha);
        text = new Color(text.getRed(), text.getGreen(), text.getBlue(), alpha);
      }

      if ((myAttributeMask & (1 << CycleView.Prop.SCALE_X.ordinal())) != 0) {
        at = new AffineTransform();
        at.translate(w / 2.0, buttonCY);
        at.scale(myCycleModel.getValue(CycleView.Prop.SCALE_X, myEasedPercent), 1);
        at.translate(-w / 2.0, -buttonCY);

        g2d.transform(at);
      }

      if ((myAttributeMask & (1 << CycleView.Prop.SCALE_Y.ordinal())) != 0) {

        at = new AffineTransform();
        at.translate(buttonCX, buttonCY);
        at.scale(1, myCycleModel.getValue(CycleView.Prop.SCALE_Y, myEasedPercent));
        at.translate(-buttonCX, -buttonCY);
        g2d.transform(at);
      }

      if ((myAttributeMask & (1 << CycleView.Prop.TRANSLATION_X.ordinal())) != 0) {

        at = new AffineTransform();
        at.translate(myCycleModel.getValue(CycleView.Prop.TRANSLATION_X, myEasedPercent), 0);
        g2d.transform(at);
      }

      if ((myAttributeMask & (1 << CycleView.Prop.TRANSLATION_Y.ordinal())) != 0) {

        at = new AffineTransform();
        at.translate(0, myCycleModel.getValue(CycleView.Prop.TRANSLATION_Y, myEasedPercent));
        g2d.transform(at);
      }
      if ((myAttributeMask & (1 << CycleView.Prop.ROTATION.ordinal())) != 0) {
        at = new AffineTransform();
        at.rotate(Math.toRadians(myCycleModel.getValue(CycleView.Prop.ROTATION, myEasedPercent)), buttonCX,
            buttonCY);
        g2d.transform(at);
      }

    }

    int px = (int) (0.5 + buttonCX - BUTTON_WIDTH / 2.0f);
    int py = (int) (0.5 + buttonCY - BUTTON_HEIGHT / 2.0f);

    g2d.setColor(background);
    g2d.fillRoundRect(px, py, BUTTON_WIDTH, BUTTON_HEIGHT, 5, 5);
    g2d.setColor(border);
    g2d.drawRoundRect(px, py, BUTTON_WIDTH, BUTTON_HEIGHT, 5, 5);
    int sw = g.getFontMetrics().stringWidth(myTitle);
    int fh = g.getFontMetrics().getHeight();
    int fa = g.getFontMetrics().getAscent();
    g2d.setColor(text);
    g2d.drawString(myTitle, px + BUTTON_WIDTH / 2 - sw / 2, py + BUTTON_HEIGHT / 2 + fa - fh / 2);
    if (time - last_update > 1000) {
      if (RENDERING_STATS) {

        System.out.println("render" + 1000 * call_count / (time - last_update) + "fps paint "
            + 1000 * paint_count / (time - last_update) + "fps");
      }
      last_update = time;
      paint_count = 0;
      call_count = 0;
    }
  }

  static String[] MOVE_NAMES = {"Stationary", "South to North", "West to East", "SW to NE",
      "NW to SE", "SE to NW", "NE to SW"};
  static String[] DURATION = {"0.1s", "0.5s", "1.0s", "2.0s", "5s", "10s", "20s"};
  static int[] DURATION_VAL = {100, 500, 1000, 2000, 5000, 10000, 20000};

  static int[][] movement = {
      {0, 0},
      {0, -1},
      {1, 0},
      {1, -1},
      {1, 1},
      {-1, -1},
      {-1, 1},
  };
  int myMoveObject = 0;

  public void setMovement(int move) {
    myMoveObject = move;
    repaint();
  }

  public void setDurationIndex(int selectedIndex) {
    mDuration = DURATION_VAL[selectedIndex];
  }
}
