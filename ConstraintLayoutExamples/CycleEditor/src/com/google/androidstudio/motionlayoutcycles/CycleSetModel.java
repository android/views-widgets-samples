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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The management of a collection of Cycles which contain the
 * model, view (graph) and the controller ui panel.
 */
public class CycleSetModel {

  private static boolean DEBUG = false;
  public MainPanel myMainPanel;
  ArrayList<Cycle> myCycles = new ArrayList<>();
  public JTextArea myXmlOutput;

  public void removeCycle(Cycle cycle) {
    myCycles.remove(cycle);
  }

  static class Cycle {

    CycleSetModel myModelSet;
    CycleModel myModel;
    CycleView myView;
    CycleEdit myControl;

    public Cycle(CycleSetModel set) {
      myModelSet = set;
      myView = new CycleView(this);
      myModel = new CycleModel(this);

    }
  }

  public Cycle createCycle() {
    Cycle cycle = new Cycle(this);
    myCycles.add(cycle);
    return cycle;
  }

  public CycleSetModel(JTextArea xmlOutput) {
    myXmlOutput = xmlOutput;
  }

  public int getAttributeMask() {
    int mask = 0;
    for (Cycle cycle : myCycles) {
      mask |= 1 << cycle.myModel.mAttrIndex;
    }
    return mask;
  }

  public double getValue(CycleView.Prop pathRotate, float x) {
    int index = pathRotate.ordinal();
    for (Cycle cycle : myCycles) {
      if (cycle.myModel.mAttrIndex == index) {
        return cycle.myModel.getComputedValue(x);
      }
    }
    return 0;
  }

  public void setDot(float x) {
    if (Float.isNaN(x)) {
      for (Cycle myCycle : myCycles) {
        myCycle.myModel.setDot(x, 0);
      }
      return;
    }
    for (Cycle myCycle : myCycles) {
      float val = myCycle.myModel.getComputedValue(x);
      myCycle.myModel.setDot(x, val);
    }

  }

  Timer timer;

  public void generateXML(CycleModel cycleModel) {
    String str = "<KeyFrameSet>\n\n";
    int offset = str.length(), tmpOffset = str.length();

    for (Cycle cycle : myCycles) {
      tmpOffset = str.length();
      str += cycle.myModel.getKeyFrames();
      if (cycleModel == cycle.myModel) {
        offset = tmpOffset;
      }

    }
    str += "</KeyFrameSet>\n";
    myXmlOutput.setText(str);

    int start = cycleModel.start_caret + offset;
    int end = cycleModel.end_caret + offset;
    myXmlOutput.setCaretPosition(start);

    ;
    SwingUtilities.invokeLater(() -> {
      myXmlOutput.setCaretPosition(end);
    });
    if (timer != null) {
      timer.stop();
    }
    timer = new Timer(1000, (e) -> {
      myXmlOutput.requestFocus();
      myXmlOutput.select(start, end);
      timer = null;
    });
    timer.setRepeats(false);
    timer.start();

  }

  public void parse() {
    String str = myXmlOutput.getText();
    HashSet<CycleView.Prop> props = preParse(str);
    if (DEBUG) {
      System.out.println("types  " + Arrays.toString(props.toArray()));
    }
    ArrayList<Cycle> unused = new ArrayList<>(myCycles);
    CycleView.Prop[] table = CycleView.Prop.values();
    Cycle[] cycles = myCycles.toArray(new Cycle[0]);
    for (int i = 0; i < cycles.length; i++) {
      Cycle cycle = cycles[i];
      CycleView.Prop p = table[cycle.myModel.mAttrIndex];
      if (DEBUG) {
        System.out.println("-- cycle " + table[cycle.myModel.mAttrIndex].name());
      }
      if (props.contains(p)) {
        unused.remove(cycle);
        if (DEBUG) {
          System.out.println("parse 1");
        }
        cycle.myModel.parseXML(str);
        if (DEBUG) {
          System.out.println("done parse 1 ");
          System.out.println("update name to = " + cycle.myModel.getAttName());
        }
        // cycle.myControl.updateTabName(cycle.myModel.getAttName());
        cycle.myControl.attrName.setSelectedIndex(cycle.myModel.mAttrIndex);
        props.remove(p);
      } else {
        if (DEBUG) {
          System.out.println(" not found");
        }
      }
    }

    // if you have used all props and tabs
    if (props.size() == 0 && unused.size() == 0) { // all used up we are done
      if (DEBUG) {
        System.out.println("all done");
      }
      return;
    }
    if (DEBUG) {
      System.out.println("props.size() ==" + props.size() + "  unused.size() == " + unused.size());
    }
    ArrayList<CycleView.Prop> propList = new ArrayList<>(props);

    while (propList.size() > 0 && unused.size() > 0) {
      Cycle cycle = unused.remove(0);
      int propIndex = propList.remove(0).ordinal();
      if (DEBUG) {
        System.out.println(
            "1reuse unused ==" + table[cycle.myModel.mAttrIndex].name() + "  unused.size() == "
                + table[propIndex].name());
      }
      cycle.myModel.mAttrIndex = propIndex;
      if (DEBUG) {
        System.out.println("parse 2");
      }
      cycle.myModel.parseXML(str);
      if (DEBUG) {
        System.out.println("done parse 2");
        System.out.println(
            "2reuse unused ==" + table[cycle.myModel.mAttrIndex].name() + "  unused.size() == "
                + table[propIndex].name());
        System.out.println("update name to = " + cycle.myModel.getAttName());

      }
      // cycle.myControl.updateTabName(cycle.myModel.getAttName());
      cycle.myControl.attrName.setSelectedIndex(cycle.myModel.mAttrIndex);
    }

    while (propList.size() > 0) {
      int attrIndex = propList.remove(0).ordinal();

      Cycle cycle = myMainPanel.createNewCycle();
      cycle.myModel.mAttrIndex = attrIndex;
      if (DEBUG) {
        System.out.println("parse 3");
      }
      cycle.myModel.parseXML(str);
      if (DEBUG) {
        System.out.println("done parse 3");
        System.out.println("update name to = " + cycle.myModel.getAttName());

      }
      // cycle.myControl.updateTabName(cycle.myModel.getAttName());
      cycle.myControl.attrName.setSelectedIndex(cycle.myModel.mAttrIndex);
    }

    while (unused.size() > 0) {
      Cycle cycle = unused.remove(0);
      System.out.println("remove " + table[cycle.myModel.mAttrIndex].name());

      myMainPanel.removeCycle(cycle);
    }
  }

  public void rebuildIndexes() {
    Cycle[] cycles = new Cycle[myCycles.size()];
    for (Cycle c : myCycles) {
      int index = MainPanel.getTabbIndex(c.myControl);
      cycles[index] = c;
    }
    myCycles = new ArrayList<Cycle>(Arrays.asList(cycles));
  }

  private HashSet<CycleView.Prop> preParse(String str) {
    HashSet<CycleView.Prop> props = new HashSet<>();
    try {
      InputStream inputStream = new ByteArrayInputStream(str.getBytes(Charset.forName("UTF-8")));
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputStream, new DefaultHandler() {
        public void startElement(String uri, String localName,
            String qName, Attributes attributes)
            throws SAXException {
          if ("KeyCycle".equals(qName)) {

            for (int i = 0; i < attributes.getLength(); i++) {
              String str = attributes.getQName(i);
              CycleView.Prop valueType;

              switch (str) {
                case "motion:transitionPathRotate":
                  props.add(CycleView.Prop.PATH_ROTATE);
                  break;
                case "android:alpha":
                  props.add(CycleView.Prop.ALPHA);
                  break;
                case "android:elevation":
                  props.add(CycleView.Prop.ELEVATION);
                  break;
                case "android:rotation":
                  props.add(CycleView.Prop.ROTATION);
                  break;
                case "android:rotationX":
                  props.add(CycleView.Prop.ROTATION_X);
                  break;
                case "android:rotationY":
                  props.add(CycleView.Prop.ROTATION_Y);
                  break;
                case "android:scaleX":
                  props.add(CycleView.Prop.SCALE_X);
                  break;
                case "android:scaleY":
                  props.add(CycleView.Prop.SCALE_Y);
                  break;
                case "android:translationX":
                  props.add(CycleView.Prop.TRANSLATION_X);
                  break;
                case "android:translationY":
                  props.add(CycleView.Prop.TRANSLATION_Y);
                  break;
                case "android:translationZ":
                  props.add(CycleView.Prop.TRANSLATION_Z);
                  break;
                case "motion:progress":
                  props.add(CycleView.Prop.PROGRESS);
                  break;
              }
            }
          }
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
    return props;
  }

}

