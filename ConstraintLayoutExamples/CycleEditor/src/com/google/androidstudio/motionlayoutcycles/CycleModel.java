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

import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The CycleModel of a collection of  KeyCycles for a single property
 */
class CycleModel {

  double[][] values = {
      {0, 0.2, 0.5, 0.7, 1},
      {0, 2, 0, 3, 1},
      {20, 360, 360, 360, 0},
      {0, 0, 0, 0, 0}
  };
  final int POS = 0;
  final int PERIOD = 1;
  final int AMP = 2;
  final int OFFSET = 3;
  public int selected = 3;
  ActionListener listener;
  CycleView mCycleView;
  CycleSetModel.Cycle myCycle;
  public JTextField mKeyCycleNo;
  private JComboBox<String> mMode;
  private int mModeValue;
  JButton delete, add;
  public JSlider mPos, mPeriod, mAmp, mOff;

  CycleModel(CycleSetModel.Cycle cycle) {
    myCycle = cycle;
  }

  boolean inCallBack = false;
  String[] waveShapeName = {
      "sin", "square", "triangle", "sawtooth", "reverseSawtooth", "cos", "bounce"
  };
  int mAttrIndex = 3;
  private JTextField mTarget;

  public void addActionListener(ActionListener listener) {
    this.listener = listener;
  }

  public String[] getStrings() {
    String[] str = new String[values[POS].length];
    for (int i = 0; i < str.length; i++) {
      str[i] = "" + i;

    }
    return str;
  }

  public void delete() {
    double[][] nv = new double[values.length][values[POS].length - 1];
    for (int i = 0; i < values.length; i++) {
      int k = 0;
      for (int j = 0; j < values[i].length; j++) {
        if (selected != j) {
          nv[i][k] = values[i][j];
          k++;
        }
      }
    }
    values = nv;
    if (selected == nv[POS].length) {
      selected--;
    }
    update();
  }

  /**
   *
   */
  public void add() {
    double[][] nv = new double[values.length][values[POS].length + 1];
    for (int i = 0; i < values.length; i++) {
      int k = 0;
      for (int j = 0; j < values[i].length; j++) {
        if (selected == j) {
          nv[i][k] = values[i][j];
          if (j > 0) {
            nv[i][k] = (values[i][j] + values[i][j - 1]) / 2;
          }
          k++;
          nv[i][k] = values[i][j];
          if (j < values[i].length - 1) {
            nv[i][k] = (values[i][j] + values[i][j + 1]) / 2;
          }
          k++;
          continue;
        }
        nv[i][k] = values[i][j];
        k++;
      }
    }
    values = nv;
    if (selected == nv[POS].length) {
      selected--;
    }
    update();
  }

  public void setCycle(CycleView cycleView) {
    mCycleView = cycleView;
  }

  public void update() {
    updateUIelements();
    mCycleView.setCycle(0, values[POS], values[PERIOD], values[AMP], values[OFFSET], selected,
        mModeValue);
    generateXML();
    mCycleView.repaint();
  }

  public float getComputedValue(float v) {
    return mCycleView.getComputedValue(v);
  }

  public void setDot(float x, float y) {
    mCycleView.setDot(x, y);
  }

  /**
   *
   */
  public void setPos() {
    int v = mPos.getValue();
    if (inCallBack) {
      return;
    }

    int min = v;
    int max = v;
    if (selected > 0) {
      min = 1 + (int) (0.5 + (100 * values[POS][selected - 1]));
    }
    if (selected < values[POS].length - 1) {
      max = ((int) (0.5 + (100 * values[POS][selected + 1]))) - 1;
    }
    double pvalue = Math.max(min, Math.min(v, max)) / 100f;
    values[POS][selected] = pvalue;
    update();
  }

  public void setPeriod() {
    if (inCallBack) {
      return;
    }
    values[PERIOD][selected] = mPeriod.getValue();
    update();
  }

  public void setAmp() {
    if (inCallBack) {
      return;
    }
    int val = mAmp.getValue();
    if (CycleView.MainAttribute.mapTo100[mAttrIndex]) {
      float min = CycleView.MainAttribute.typicalRange[mAttrIndex][0];
      float max = CycleView.MainAttribute.typicalRange[mAttrIndex][1];
      values[AMP][selected] = val * (max - min) / 100 + min;

    } else {
      values[AMP][selected] = val;
    }
    update();
  }

  public void setOffset() {
    if (inCallBack) {
      return;
    }
    if (CycleView.MainAttribute.mapTo100[mAttrIndex]) {
      float min = CycleView.MainAttribute.typicalRange[mAttrIndex][0];
      float max = CycleView.MainAttribute.typicalRange[mAttrIndex][1];
      values[OFFSET][selected] = mOff.getValue() * (max - min) / 100 + min;
    } else {
      values[OFFSET][selected] = mOff.getValue();
    }
    update();
  }

  void setMode() {
    mModeValue = mMode.getSelectedIndex();
    update();
  }

  public void setSelected(int selectedIndex) {
    inCallBack = true;
    selected = selectedIndex;

    update();
    inCallBack = false;
  }

  public void setUIElements(JSlider pos, JSlider period, JSlider amp, JSlider off,
      JComboBox<String> mode) {
    mPeriod = period;
    mOff = off;
    mAmp = amp;
    mPos = pos;
    mMode = mode;
    mPeriod.setMinimum(0);
    mPeriod.setMaximum(9);

    mOff.setPaintTicks(true);
    mPos.setPaintTicks(true);
    mAmp.setPaintTicks(true);
    mPeriod.setPaintTicks(true);

    mPeriod.setMajorTickSpacing(1);

    mPos.setMinorTickSpacing(5);
    mPos.setMajorTickSpacing(10);

    mOff.setMinorTickSpacing(10);
    mPeriod.setPaintTicks(true);
    mOff.setPaintLabels(true);
    mAmp.setPaintLabels(true);
    mPeriod.setPaintLabels(true);
    mPos.addChangeListener((e) ->
        setPos());
    mPeriod.addChangeListener((e) ->
        setPeriod());
    mOff.addChangeListener((e) ->
        setOffset());
    mAmp.addChangeListener((e) ->
        setAmp());
    mMode.addActionListener((e) ->
        setMode());
    updateUIelements();

  }

  void updateUIelements() {
    inCallBack = true;
    int max = (int) (0.5 + 100 * ((selected == values[POS].length - 1) ? 1
        : (values[POS][selected + 1])));
    int min = (int) (0.5 + 100 * (selected == 0 ? 0 : (values[POS][selected - 1])));
    boolean middle = (selected > 0) && (selected < values[POS].length - 1);
    mPos.setEnabled(middle);
    delete.setEnabled(middle);

    mPos.setMaximum(max - 1);
    mPos.setMinimum(min + 1);
    if (CycleView.MainAttribute.mapTo100[mAttrIndex]) {
      mAmp.setMinimum(0);
      mAmp.setMaximum(100);
      mOff.setMinimum(-100);
      mOff.setMaximum(100);
      Hashtable<Integer, JLabel> labelTable =
          new Hashtable<Integer, JLabel>();
      labelTable.put(0, new JLabel("" + CycleView.MainAttribute.typicalRange[mAttrIndex][0]));
      labelTable.put(50, new JLabel("" +
          (CycleView.MainAttribute.typicalRange[mAttrIndex][0] + CycleView.MainAttribute.typicalRange[mAttrIndex][1])
              / 2));
      labelTable.put(100, new JLabel("" + CycleView.MainAttribute.typicalRange[mAttrIndex][1]));
      mOff.setLabelTable(labelTable);
      mAmp.setLabelTable(labelTable);
    } else {
      mAmp.setMinimum((int) CycleView.MainAttribute.typicalRange[mAttrIndex][0]);
      mAmp.setMaximum((int) CycleView.MainAttribute.typicalRange[mAttrIndex][1]);
      mOff.setMinimum((int) CycleView.MainAttribute.typicalRange[mAttrIndex][0]);
      mOff.setMaximum((int) CycleView.MainAttribute.typicalRange[mAttrIndex][1]);
      mOff.setLabelTable(null);
      mAmp.setLabelTable(null);

    }

    if (CycleView.MainAttribute.mapTo100[mAttrIndex]) {
      mAmp.setPaintTicks(true);
      mAmp.setPaintLabels(true);
      mAmp.setMinorTickSpacing(10);
      mAmp.setMajorTickSpacing(100);

      mOff.setMinorTickSpacing(10);
      mOff.setMajorTickSpacing(100);
      mAmp.repaint();

    } else {
      int maxr = (int) (CycleView.MainAttribute.typicalRange[mAttrIndex][1]);
      mAmp.setPaintTicks(true);
      mAmp.setPaintLabels(true);
      mAmp.setMinorTickSpacing(90);
      mAmp.setMajorTickSpacing(180);
      mAmp.repaint();
      mOff.setMinorTickSpacing(90);
      mOff.setMajorTickSpacing(180);
      mAmp.setPaintTicks(true);
      mAmp.setPaintLabels(true);

    }
    mPos.setValue((int) (values[POS][selected] * 100));
    mPeriod.setValue((int) (values[PERIOD][selected]));
    if (CycleView.MainAttribute.mapTo100[mAttrIndex]) {
      float range_min = CycleView.MainAttribute.typicalRange[mAttrIndex][0];
      float range_max = CycleView.MainAttribute.typicalRange[mAttrIndex][1];
      mAmp.setValue(
          (int) (0.5 + 100 * (values[AMP][selected] / (range_max - range_min) + range_min)));
      mOff.setValue(
          (int) (0.5 + 100 * (values[OFFSET][selected] / (range_max - range_min) + range_min)));

    } else {
      mAmp.setValue((int) (0.5 + values[AMP][selected]));
      mOff.setValue((int) (0.5 + values[OFFSET][selected]));
    }
    inCallBack = false;

  }

  public void changeSelection(int delta) {
    int length = values[POS].length;
    selected = (delta + selected + length) % length;
    mKeyCycleNo.setText("" + selected);

    update();
  }

  class ParseResults {

    double[][] values = new double[4][0];
    final int POS = 0;
    final int PERIOD = 1;
    final int AMP = 2;
    final int OFFSET = 3;

    int current = -1;
    double tmpPos;
    double tmpPeriod;
    double tmpValue;
    double tmpOffset;
    String tmpTarget;
    int tmpShape;
    CycleView.Prop tmpValueType;

    void add() {
      for (int i = 0; i < values.length; i++) {
        values[i] = Arrays.copyOf(values[i], values[i].length + 1);
      }
      current = values[0].length - 1;
      values[POS][current] = tmpPos;
      values[PERIOD][current] = tmpPeriod;
      values[AMP][current] = tmpValue;
      values[OFFSET][current] = tmpOffset;
      target = tmpTarget;
      shape = tmpShape;
      valueType = tmpValueType;

    }

    void setFramePosition(double v) {
      tmpPos = v;
    }

    void setWavePeriod(double v) {
      tmpPeriod = v;
    }

    void setWaveValue(double v) {
      tmpValue = v;
    }

    void setWaveOffset(double v) {
      tmpOffset = v;
    }

    void setTarget(String v) {
      tmpTarget = v;
    }

    void setShape(int v) {
      tmpShape = v;
    }

    void setValueType(CycleView.Prop v) {
      tmpValueType = v;
    }

    String target;
    int shape;
    CycleView.Prop valueType;
  }

  public static String trimDp(String v) {
    if (v.lastIndexOf("dp") != -1) {
      return v.substring(0, v.lastIndexOf("dp"));
    }
    return v;
  }

  public void parseXML(String str) {
    try {
      InputStream inputStream = new ByteArrayInputStream(str.getBytes(Charset.forName("UTF-8")));
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      ParseResults results = new ParseResults();
      saxParser.parse(inputStream, new DefaultHandler() {
        public void startElement(String uri, String localName,
            String qName, Attributes attributes)
            throws SAXException {
          if ("KeyCycle".equals(qName)) {

            for (int i = 0; i < attributes.getLength(); i++) {
              switch (attributes.getQName(i)) {
                case "motion:framePosition":
                  results.setFramePosition(Integer.parseInt(attributes.getValue(i)) / 100f);
                  break;
                case "motion:target":
                  results.setTarget(attributes.getValue(i).substring(5));
                  break;
                case "motion:wavePeriod":
                  results.setWavePeriod(Float.parseFloat(attributes.getValue(i)));
                  break;
                case "motion:waveOffset":
                  results.setWaveOffset(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "motion:waveShape":
                  String shape = attributes.getValue(i);
                  for (int j = 0; j < waveShapeName.length; j++) {
                    if (waveShapeName[j].equals(shape)) {
                      results.setShape(j);
                    }
                  }
                  break;
                case "motion:transitionPathRotate":
                  results.setValueType(CycleView.Prop.PATH_ROTATE);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:alpha":
                  results.setValueType(CycleView.Prop.ALPHA);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:elevation":
                  results.setValueType(CycleView.Prop.ELEVATION);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:rotation":
                  results.setValueType(CycleView.Prop.ROTATION);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:rotationX":
                  results.setValueType(CycleView.Prop.ROTATION_X);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:rotationY":
                  results.setValueType(CycleView.Prop.ROTATION_Y);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:scaleX":
                  results.setValueType(CycleView.Prop.SCALE_X);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:scaleY":
                  results.setValueType(CycleView.Prop.SCALE_Y);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:translationX":
                  results.setValueType(CycleView.Prop.TRANSLATION_X);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:translationY":
                  results.setValueType(CycleView.Prop.TRANSLATION_Y);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "android:translationZ":
                  results.setValueType(CycleView.Prop.TRANSLATION_Z);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
                case "motion:progress":
                  results.setValueType(CycleView.Prop.PROGRESS);
                  results.setWaveValue(Float.parseFloat(trimDp(attributes.getValue(i))));
                  break;
              }
            }
            if (results.tmpValueType.ordinal() == mAttrIndex) {
              results.add();
            }
          }

        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException {

        }
      });
      values = results.values;
      selected = values[POS].length / 2;
      mMode.setSelectedIndex(results.shape);
      mTarget.setText(results.target);
      mAttrIndex = results.valueType.ordinal();
      update();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public int start_caret = 0;
  public int end_caret = 0;

  public String getKeyFrames() {
    String str = "\n";
    start_caret = 1;
    end_caret = 1;
    String target = mTarget.getText();
    for (int i = 0; i < values[POS].length; i++) {
      double pos = values[POS][i];
      double per = values[PERIOD][i];
      double amp = values[AMP][i];
      double off = values[OFFSET][i];
      String xmlstr = "<KeyCycle \n";
      xmlstr += "        motion:framePosition=\"" + (int) (0.5 + pos * 100) + "\"\n";
      xmlstr += "        motion:target=\"@+id/" + target + "\"\n";
      xmlstr += "        motion:wavePeriod=\"" + (int) (per) + "\"\n";
      xmlstr += "        motion:waveOffset=\"" + CycleView.MainAttribute.process(off, mAttrIndex) + "\"\n";
      xmlstr += "        motion:waveShape=\"" + waveShapeName[mMode.getSelectedIndex()] + "\"\n";
      xmlstr += "        " + CycleView.MainAttribute.Names[mAttrIndex] + "=\"" + CycleView.MainAttribute
          .process(amp, mAttrIndex) + "\"/>\n\n";
      if (selected == i) {
        start_caret = str.length();
      }
      str += xmlstr;
      if (selected == i) {
        end_caret = str.length();
      }
    }
    return str;
  }

  public void generateXML() {
    myCycle.myModelSet.generateXML(this);
  }

  String getAttName() {
    return CycleView.MainAttribute.ShortNames[mAttrIndex];
  }

  public void setAttr(int selectedIndex) {
    int old = mAttrIndex;

    mAttrIndex = selectedIndex;
    float old_min = CycleView.MainAttribute.typicalRange[old][0];
    float old_max = CycleView.MainAttribute.typicalRange[old][1];
    float new_min = CycleView.MainAttribute.typicalRange[mAttrIndex][0];
    float new_max = CycleView.MainAttribute.typicalRange[mAttrIndex][1];
    for (int i = 0; i < values[AMP].length; i++) {
      double value = values[AMP][i];
      values[AMP][i] =
          ((new_max - new_min) * ((value - old_min) / (old_max - old_min))) + new_min;
    }

    update();

  }

  public void selectClosest(Point2D p) {
    double min = Double.MAX_VALUE;
    int mini = -1;
    for (int i = 0; i < values[POS].length; i++) {
      double dist = p.distance(values[POS][i], values[OFFSET][i]);
      if (min > dist) {
        mini = i;
        min = dist;
      }
    }
    if (mini != -1) {
      selected = mini;
      mKeyCycleNo.setText("" + selected);
      update();
    }
  }

  public void setTarget(JTextField target) {
    mTarget = target;
  }

}
