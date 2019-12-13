

package com.example.tankwar.entity;

import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.enums.TankTypeEnum;

import java.awt.Color;

/**
 * MyTank...
 */
public class MyTank extends Tank {
    /**
     * 构造方法
     *
     * @param x      x坐标
     * @param y      y坐标
     * @param direct 方向
     */
    public MyTank(int x, int y, DirectionEnum direct) {
        super(x, y, direct);
        this.setColor(Color.yellow);
        this.setTankType(TankTypeEnum.MY);
        this.setBlood(10);
    }


}