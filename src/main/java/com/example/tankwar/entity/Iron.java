

package com.example.tankwar.entity;

import com.example.tankwar.enums.StuffTypeEnum;

/**
 * Iron...
 */
public class Iron extends Stuff {
    /**
     * 构造方法
     *
     * @param x x坐标
     * @param y y坐标
     */
    public Iron(int x, int y) {
        super(x, y);
        this.setType(StuffTypeEnum.IRON);
        this.setWidth(20);
        this.setHeight(20);
    }

}
