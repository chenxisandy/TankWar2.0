package com.example.tankwar.entity;


public class Bomb {
    /**
     * 炸弹的宽度
     */
    private int l;
    /**
     * 炸弹的x坐标
     */
    private int x;
    /**
     * 炸弹的y坐标
     */
    private int y;
    /**
     * 炸弹的时间，决定它的射程
     */
    private int lifeTime = 30;
    /**
     * 炸弹是否活着
     */
    private boolean isLive = true;

    /**
     * 炸弹的构造函数
     *
     * @param x 炸弹的x坐标
     * @param y 炸弹的y坐标
     */
    public Bomb(int x, int y) { // 构造函数，初始化工作
        this.x = x;
        this.y = y;
    }

    /**
     * 存活时间越久，在游戏面板的显示时间越长
     */
    public void lifeDown() {
        if (lifeTime > 0) {
            lifeTime--;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }
}
