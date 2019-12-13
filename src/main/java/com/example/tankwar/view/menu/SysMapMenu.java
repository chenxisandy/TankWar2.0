package com.example.tankwar.view.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * LevelMenu...
 */
public class SysMapMenu extends JMenu {

    public SysMapMenu(ActionListener listener) {
        super("系统地图");
        JMenuItem firstLevel = new JMenuItem("第一关");
        JMenuItem secondLevel = new JMenuItem("第二关");
        JMenuItem thirdLevel = new JMenuItem("第三关");
        JMenuItem fourthLevel = new JMenuItem("第四关");
        JMenuItem fifthLevel = new JMenuItem("第五关");

        firstLevel.setActionCommand("first");
        secondLevel.setActionCommand("second");
        thirdLevel.setActionCommand("third");
        fourthLevel.setActionCommand("fourth");
        fifthLevel.setActionCommand("fifth");

        firstLevel.addActionListener(listener);
        secondLevel.addActionListener(listener);
        thirdLevel.addActionListener(listener);
        fourthLevel.addActionListener(listener);
        fifthLevel.addActionListener(listener);

        this.add(firstLevel);
        this.add(secondLevel);
        this.add(thirdLevel);
        this.add(fourthLevel);
        this.add(fifthLevel);
    }
}
