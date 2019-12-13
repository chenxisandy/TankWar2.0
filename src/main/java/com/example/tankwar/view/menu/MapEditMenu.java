package com.example.tankwar.view.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Class Description...
 */
public class MapEditMenu extends JMenu {
    public MapEditMenu(ActionListener listener) {
        super("地图编辑");
        JMenuItem createMap = new JMenuItem("地图编辑");
        JMenuItem saveMap = new JMenuItem("地图保存");

        createMap.setActionCommand("createMap");
        saveMap.setActionCommand("saveMap");

        createMap.addActionListener(listener);
        saveMap.addActionListener(listener);

        this.add(createMap);
        this.add(saveMap);
    }
}
