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

import static javax.swing.SwingConstants.TRAILING;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * The gui to edit a single cycle
 */
public class CycleEdit extends JPanel {

  private static final boolean DEBUG = false;
  JComboBox<String> baseMovement = new JComboBox<>(AnimationPanel.MOVE_NAMES);
  JComboBox<String> duration = new JComboBox<>(AnimationPanel.DURATION);
  CycleModel myCycleModel;
  JComboBox<String> attrName;

  class TabUI extends JPanel {

    JButton tabButton = new JButton();
    JButton tabTitle = new JButton();

    TabUI(String name) {
      super(new BorderLayout());
      setBackground(null);
      tabButton.setBorder(null);

      add(tabTitle, BorderLayout.CENTER);
      add(tabButton, BorderLayout.EAST);
      tabTitle.setBorder(null);
      tabTitle.setFocusPainted(false);
      tabTitle.setContentAreaFilled(false);
      tabTitle.setBorderPainted(true);
      tabTitle.setBackground(null);
      tabTitle.setMargin(new Insets(0, 0, 0, 0));
      tabTitle.setText(name);

      tabButton.setBorder(null);
      tabButton.setIcon(UIManager.getIcon("InternalFrame.paletteCloseIcon"));
      tabButton.setFocusPainted(false);
      tabButton.setContentAreaFilled(false);
      tabButton.setBorderPainted(true);
      tabButton.setBackground(null);
      tabButton.setMargin(new Insets(0, 0, 0, 0));
    }

    public void addRaiseAction(ActionListener al) {
      tabTitle.addActionListener(al);
    }

    public void addKillAction(ActionListener al) {
      tabButton.addActionListener(al);
    }
  }

  void updateTabName(String name) {
    if (DEBUG) {
      System.out.println(">>>>>" + name + "  " + MainPanel.getTabbIndex(this));
      StackTraceElement[] s = new Throwable().getStackTrace();
      System.out.println("  .(" + s[1].getFileName() + ":" + s[1].getLineNumber() + ") ");
      System.out.println("  .(" + s[2].getFileName() + ":" + s[2].getLineNumber() + ") ");
    }
    Container tabb = getParent();
    while (!(tabb instanceof JTabbedPane)) {
      tabb = tabb.getParent();
    }

    JTabbedPane tabbedPane = (JTabbedPane) tabb;
    int index = MainPanel.getTabbIndex(this);
    tabbedPane.setTitleAt(index, name);
    // tabUI = new TabUI(name);
    // tabbedPane.setTabComponentAt(index, tabUI);
  }

  public void removeCycle() {
    myCycleModel.myCycle.myModelSet.removeCycle(myCycleModel.myCycle);
    listener.actionPerformed(null);
  }

  ActionListener listener;

  public void setRemoveCallback(ActionListener l) {
    listener = l;
  }

  public CycleEdit(CycleView cycleView, CycleModel cycleModel, AnimationPanel animationPanel) {
    super(new GridBagLayout());
    JPanel control = this;

    setBorder(new EmptyBorder(5, 10, 10, 20));
    myCycleModel = cycleModel;

    cycleView.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        myCycleModel.selectClosest(cycleView.last_click);
      }
    });
    BasicArrowButton next = new BasicArrowButton(BasicArrowButton.EAST);
    BasicArrowButton prev = new BasicArrowButton(BasicArrowButton.WEST);
    myCycleModel.delete = next;
    cycleView.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        myCycleModel.selectClosest(cycleView.last_click);
      }
    });
    JTextField number = new JTextField("XX");
    number.setPreferredSize(number.getPreferredSize());
    GridBagConstraints gbc = new GridBagConstraints();
    number.setText("" + myCycleModel.selected);
    number.setEditable(false);
    gbc.insets = new Insets(10, 5, 0, 0);
    next.addActionListener((e) -> {
      myCycleModel.changeSelection(+1);
    });
    prev.addActionListener((e) -> {
      myCycleModel.changeSelection(-1);
    });
    myCycleModel.mKeyCycleNo = number;

    gbc.gridy = 0;
    gbc.gridx = 2;
    gbc.gridwidth = 1;

    control.add(new JLabel("KeyCycle:", TRAILING), gbc);
    gbc.gridx = 3;
    gbc.anchor = GridBagConstraints.EAST;
    control.add(prev, gbc);
    gbc.gridx++;
    control.add(number, gbc);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.WEST;
    control.add(next, gbc);
    gbc.gridy++;

    JButton del = new JButton("Delete");
    JButton add = new JButton("Add");
    myCycleModel.add = add;
    myCycleModel.delete = del;
    del.addActionListener((e) -> {
      myCycleModel.delete();
    });
    add.addActionListener((e) -> {
      myCycleModel.add();
    });
    gbc.gridwidth = 1;
    gbc.gridx = 3;
    control.add(add, gbc);
    gbc.gridx += 2;
    control.add(del, gbc);
    gbc.gridy++;
    gbc.gridwidth = 3;
    JSlider pos = new JSlider();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    control.add(new JLabel("Pos:", TRAILING), gbc);
    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;
    control.add(pos, gbc);
    gbc.gridy++;

    JSlider period = new JSlider();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    control.add(new JLabel("Period:", TRAILING), gbc);
    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;

    control.add(period, gbc);
    gbc.gridy++;

    JSlider amp = new JSlider();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    attrName = new JComboBox<>(CycleView.MainAttribute.ShortNames);
    attrName.setSelectedIndex(myCycleModel.mAttrIndex);
    attrName.addActionListener(e -> {
      myCycleModel.setAttr(attrName.getSelectedIndex());
      updateTabName(myCycleModel.getAttName());
      animationPanel.setMode();
    });

    control.add(attrName, gbc);

    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;
    control.add(amp, gbc);

    gbc.gridy++;
    JSlider off = new JSlider();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    control.add(new JLabel("Offset:", TRAILING), gbc);

    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;
    control.add(off, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    control.add(new JLabel("WaveType:", TRAILING), gbc);

    JComboBox<String> mode = new JComboBox<>(myCycleModel.waveShapeName);
    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;
    control.add(mode, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    control.add(new JLabel("Target:", TRAILING), gbc);

    gbc.gridx += 3;
    gbc.anchor = GridBagConstraints.WEST;
    JTextField target = new JTextField("XXXXXXXXXXXXXXXXXXXXX");
    target.setPreferredSize(target.getPreferredSize());
    target.setText("button");
    control.add(target, gbc);

    myCycleModel.setTarget(target);
    myCycleModel.setUIElements(pos, period, amp, off, mode);
    myCycleModel.setCycle(cycleView);
  }
}
