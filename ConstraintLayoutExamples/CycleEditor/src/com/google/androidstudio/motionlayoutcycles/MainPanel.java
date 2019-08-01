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


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * This provide the overall frame and top level ui elements
 */
public class MainPanel extends JFrame {

    JTextArea myXmlOutput = new JTextArea();
    JScrollPane myXmlScrollPane = new JScrollPane(myXmlOutput);

    JButton myPlayButton = new JButton("Play");
    JComboBox<String> baseMovement = new JComboBox<>(AnimationPanel.MOVE_NAMES);
    JComboBox<String> duration = new JComboBox<>(AnimationPanel.DURATION);
    JComboBox<String> easing = new JComboBox<>(AnimationPanel.EASING_OPTIONS);


    JPanel main = new JPanel(new BorderLayout(5,5));
    JPanel main1 = new JPanel(new BorderLayout(5,5));
    JPanel main2 = new JPanel(new BorderLayout(5,5));


    GridLayout myGraphLayout = new GridLayout(1, 1);
    JPanel myGraphs = new JPanel(myGraphLayout);

    JMenuBar topMenu = new JMenuBar();
    JPanel base = new JPanel(new BorderLayout());
    JTabbedPane myCycleEditTabs = new JTabbedPane();
    CycleSetModel myCycleSetModel = new CycleSetModel(myXmlOutput);

    private CycleSetModel.Cycle createCycle() {
        return myCycleSetModel.createCycle();
    }

    AnimationPanel animationPanel = new AnimationPanel(myCycleSetModel, myPlayButton);

    public static JButton createTabbButton(String text) {
        JButton ret = new JButton(text);
        ret.setBorder(null);
        if (text == null) {
            ret.setIcon(UIManager.getIcon("InternalFrame.paletteCloseIcon"));
        }
        ret.setFocusPainted(false);
        ret.setContentAreaFilled(false);
        ret.setBorderPainted(true);
        ret.setBackground(null);
        ret.setHorizontalTextPosition(SwingConstants.LEFT);
        ret.setMargin(new Insets(0, 0, 0, 0));
        return ret;
    }

    MainPanel() {
        super("Cycle Editor");
        setBounds(new Rectangle(1000, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCycleSetModel.myMainPanel = this;

        CycleSetModel.Cycle myCycle;
        CycleEdit cycleEdit;

        myCycle = createCycle();

        myGraphs.add(myCycle.myView);
        main.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        main.add(main1,BorderLayout.CENTER);

        main.add(main2,BorderLayout.SOUTH);
        main2.add(animationPanel,BorderLayout.CENTER);
        main1.add(myGraphs,BorderLayout.CENTER);
        main2.add(myXmlScrollPane,BorderLayout.EAST);
        main1.add(base,BorderLayout.EAST);

        myXmlScrollPane.setPreferredSize(new Dimension(100, 300));
        BasicArrowButton next = new BasicArrowButton(BasicArrowButton.EAST);
        BasicArrowButton prev = new BasicArrowButton(BasicArrowButton.WEST);
        myCycle.myModel.delete = next;

        // add the first panel
        cycleEdit = new CycleEdit(myCycle.myView, myCycle.myModel, animationPanel);
        myCycle.myControl = cycleEdit;
        JScrollPane scrollPane = new JScrollPane(cycleEdit);
        myCycleEditTabs.add(scrollPane);
        cycleEdit.updateTabName(myCycle.myModel.getAttName());

        // add the add more panel
        myCycleEditTabs.add(new JPanel(), "+");
        JButton newTabbButton = createTabbButton("+");
        newTabbButton.addActionListener(e -> createNewCycle());
        myCycleEditTabs.setTabComponentAt(1, newTabbButton);

        base.add(myCycleEditTabs);

        JPanel bottomControls = new JPanel();
        base.add(bottomControls, BorderLayout.SOUTH);

        myPlayButton.setText("XXXXX");
        myPlayButton.setPreferredSize(myPlayButton.getPreferredSize());
        myPlayButton.setText("Play");
        bottomControls.add(myPlayButton);
        baseMovement
                .addActionListener((e) -> animationPanel.setMovement(baseMovement.getSelectedIndex()));
        bottomControls.add(baseMovement);

        duration.setSelectedIndex(AnimationPanel.DURATION.length - 1);
        duration.addActionListener((e) -> animationPanel.setDurationIndex(duration.getSelectedIndex()));
        bottomControls.add(duration);

        easing.addActionListener((e) -> animationPanel.setEasing((String) easing.getSelectedItem()));
        bottomControls.add(easing);

        myXmlScrollPane.setPreferredSize(base.getPreferredSize());

        setContentPane(main);

        validate();

        myCycle.myModel.update();
        JMenu menu = new JMenu("File");
        topMenu.add(menu);
        JMenuItem menuItem = new JMenuItem("parse xml", KeyEvent.VK_T);
        menuItem.addActionListener(e -> myCycle.myModelSet.parse());
        menu.add(menuItem);

        menu = new JMenu("Examples");
        topMenu.add(menu);
        for (int i = 0; i < KeyCycleExamples.all.length; i++) {
            String text = KeyCycleExamples.all[i][1];
            int speed = Integer.parseInt(KeyCycleExamples.all[i][2]);
            int movement = Integer.parseInt(KeyCycleExamples.all[i][3]);
            int easingType = Integer.parseInt(KeyCycleExamples.all[i][4]);
            menuItem = new JMenuItem(KeyCycleExamples.all[i][0]);
            menuItem.addActionListener(e -> {
                myXmlOutput.setText(text);
                duration.setSelectedIndex(speed);
                baseMovement.setSelectedIndex(movement);
                easing.setSelectedIndex(easingType);
                myCycle.myModelSet.parse();
                animationPanel.play();
            });
            menu.add(menuItem);
        }

        menu = new JMenu("Cycle");
        menuItem = new JMenuItem("Add cycle", KeyEvent.VK_A);
        menuItem.addActionListener(e -> createNewCycle());
        menu.add(menuItem);
        menuItem = new JMenuItem("Remove cycle", KeyEvent.VK_R);
        menuItem.addActionListener(e -> removeCurrentCycle());
        menu.add(menuItem);
        topMenu.add(menu);

        menuItem = new JMenuItem("Play", KeyEvent.VK_P);
        menuItem.addActionListener(e -> animationPanel.play());
        topMenu.add(menuItem);

        this.setJMenuBar(topMenu);
    }

    public CycleSetModel.Cycle createNewCycle() {
        CycleSetModel.Cycle cycle = createCycle();
        CycleEdit cycleEdit = new CycleEdit(cycle.myView, cycle.myModel, animationPanel);
        cycle.myControl = cycleEdit;
        cycleEdit.setRemoveCallback(e -> removeCycle(cycle));
        int count = myCycleEditTabs.getTabCount();
        myCycleEditTabs.insertTab("label", null, cycleEdit, "tooltip", count - 1);
        cycleEdit.updateTabName(cycle.myModel.getAttName());
        myGraphs.add(cycle.myView);
        myGraphLayout.setRows(myCycleEditTabs.getTabCount() - 1);
        myGraphs.validate();
        return cycle;
    }

    public static int getTabbIndex(JComponent component) {
        Container tabb = component.getParent();
        Component lastComponent = component;
        while (!(tabb instanceof JTabbedPane)) {
            lastComponent = tabb;
            tabb = tabb.getParent();
        }
        return ((JTabbedPane) tabb).indexOfComponent(lastComponent);
    }

    void removeCurrentCycle() {
        int i = myCycleEditTabs.getSelectedIndex();
        removeCycle(myCycleSetModel.myCycles.get(i));
    }

    void removeCycle(CycleSetModel.Cycle cycle) {
        if (myCycleEditTabs.getTabCount() < 3) { // cant remove the last one
            return;
        }
        if (cycle.myControl != null) {
            myCycleEditTabs.remove(getTabbIndex(cycle.myControl));
        }
        myGraphs.remove(cycle.myView);
        myGraphs.validate();
        myGraphLayout.setRows(myCycleEditTabs.getTabCount() - 1);
        myCycleSetModel.removeCycle(cycle);
        myCycleEditTabs.setSelectedIndex(0);

    }

    public static void main(String[] arg) {
        MainPanel f = new MainPanel();
        f.setVisible(true);
    }

}
