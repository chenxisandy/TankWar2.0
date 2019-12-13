

package com.example.tankwar.entity;

import java.awt.Color;
import java.util.Timer;

import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.enums.TankTypeEnum;


public class EnemyTank extends Tank {
    /**
     * 敌人坦克刚出现时的位置，5个固定地方
     */
    private int location;
    /**
     * 我的坦克在敌人坦克的相对位置，正北方‘正南方’正西方‘正东方，-1为不知道
     */
    private DirectionEnum myTankLocation = DirectionEnum.INVALID;
    /**
     * 我的坦克方向
     */
    private DirectionEnum myTankDirect = DirectionEnum.NORTH;
    /**
     * 定时器
     */
    private Timer timer;
    /**
     * 是否要开火
     */
    private boolean isShot = false;
    /**
     * 是否在地图中
     */
    //private boolean isInMap = false;

    /**
     * 敌人坦克构造方法
     *
     * @param x
     * @param y
     * @param direct
     */
    public EnemyTank(int x, int y, DirectionEnum direct) {
        super(x, y, direct);
        this.setSpeed(4);
        this.setTankType(TankTypeEnum.ENEMY);
        this.setDirect(DirectionEnum.NORTH);
        this.setColor(Color.red);
        this.setBlood(2);
        this.setSpeedVector(0); // 设为0表示没有保存坦克的速度，按下暂停时速度就不会是0
        timer = new Timer();

    }


    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }


    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean isShot) {
        this.isShot = isShot;
    }

    public DirectionEnum getMyTankLocation() {
        return myTankLocation;
    }

    public void setMyTankLocation(DirectionEnum myTankLocation) {
        this.myTankLocation = myTankLocation;
    }

    public DirectionEnum getMyTankDirect() {
        return myTankDirect;
    }

    public void setMyTankDirect(DirectionEnum myTankDirect) {
        this.myTankDirect = myTankDirect;
    }
}

