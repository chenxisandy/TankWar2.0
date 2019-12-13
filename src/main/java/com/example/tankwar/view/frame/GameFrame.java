package com.example.tankwar.view.frame;

import com.example.tankwar.resource.TankGameImages;

import javax.swing.*;
import java.awt.*;

/**
 * GameFrame...
 */
public class GameFrame extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1176914786963603304L;


    public GameFrame() {
        super();


        this.setSize(800, 700);
        this.setTitle("TankWar2.0");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(TankGameImages.myTankImg[0]);

        // 显示器屏幕大小
        Dimension screenSizeInfo = Toolkit.getDefaultToolkit().getScreenSize();
        int leftTopX = ((int) screenSizeInfo.getWidth() - this.getWidth()) / 2;
        int leftTopY = ((int) screenSizeInfo.getHeight() - this.getHeight()) / 2;

        // 设置显示的位置在屏幕中间
        this.setLocation(leftTopX, leftTopY);
    }

}