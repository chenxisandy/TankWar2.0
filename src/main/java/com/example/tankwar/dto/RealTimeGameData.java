

package com.example.tankwar.dto;

import com.example.tankwar.entity.Bomb;
import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.entity.MyTank;
import com.example.tankwar.enums.StuffTypeEnum;
import com.example.tankwar.resource.map.Map;

import java.util.Vector;

/**
 * Class Description...
 *
 * @author 彭龙
 */
public class RealTimeGameData {
    public RealTimeGameData() {
    }

    //我方坦克
    private Vector<MyTank> myTanks = new Vector<>();

    //敌方坦克集合
    private Vector<EnemyTank> enemies = new Vector<>();

    //存炸弹
    private Vector<Bomb> bombs = new Vector<>();

    //判断地图编辑（太复杂已放弃）
    private Boolean mapMakingFlag = Boolean.FALSE;

    private StuffTypeEnum currentStuff = StuffTypeEnum.BRICK;

    private Map map;

    private int enemyTankNum;

    private int myTankNum;

    private int beKilled;

    private int myBulletNum;

    private boolean isStart = false;

    private boolean isStop = false;

    private boolean up = false;

    private boolean down = false;

    private boolean left = false;

    private boolean right = false;

    private int level = 1;

    private boolean iconSmile;

    private int dy = 600;

    private int ky = 600;

    private int kx = 0;

    private boolean heatHome = false;

    //方向检测
    public void keyPressedDirect(Boolean up, Boolean down, Boolean left, Boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void reset() {
        //全部清除
        enemies.forEach(e->e.setLive(Boolean.FALSE));
        myTanks.clear();
        enemies.clear();
        bombs.clear();
        map = null;
    }

    public int getEnemyTankNum() {
        return enemyTankNum;
    }

    public void setEnemyTankNum(int enemyTankNum) {
        this.enemyTankNum = enemyTankNum;
    }

    public int getMyTankNum() {
        return myTankNum;
    }

    public void setMyTankNum(int myTankNum) {
        this.myTankNum = myTankNum;
    }

    public int getBeKilled() {
        return beKilled;
    }

    public void setBeKilled(int beKilled) {
        this.beKilled = beKilled;
    }

    public int getMyBulletNum() {
        return myBulletNum;
    }

    public void setMyBulletNum(int myBulletNum) {
        this.myBulletNum = myBulletNum;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isIconSmile() {
        return iconSmile;
    }

    public void setIconSmile(boolean iconSmile) {
        this.iconSmile = iconSmile;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getKy() {
        return ky;
    }

    public void setKy(int ky) {
        this.ky = ky;
    }

    public int getKx() {
        return kx;
    }

    public void setKx(int kx) {
        this.kx = kx;
    }

    public Vector<MyTank> getMyTanks() {
        return myTanks;
    }

    public void setMyTanks(Vector<MyTank> myTanks) {
        this.myTanks = myTanks;
    }

    public Vector<EnemyTank> getEnemies() {
        return enemies;
    }

    public void setEnemies(Vector<EnemyTank> enemies) {
        this.enemies = enemies;
    }

    public Vector<Bomb> getBombs() {
        return bombs;
    }

    public void setBombs(Vector<Bomb> bombs) {
        this.bombs = bombs;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "RealTimeGameData{" +
                ", enemyTankNum=" + enemyTankNum +
                ", myTankNum=" + myTankNum +
                ", beKilled=" + beKilled +
                ", myBulletNum=" + myBulletNum +
                ", isStart=" + isStart +
                ", isStop=" + isStop +
                ", up=" + up +
                ", down=" + down +
                ", left=" + left +
                ", right=" + right +
                ", level=" + level +
                ", iconSmile=" + iconSmile +
                ", dy=" + dy +
                ", ky=" + ky +
                ", kx=" + kx +
                '}';
    }

    public StuffTypeEnum getCurrentStuff() {
        return currentStuff;
    }

    public void setCurrentStuff(StuffTypeEnum currentStuff) {
        this.currentStuff = currentStuff;
    }

    public Boolean getMapMakingFlag() {
        return mapMakingFlag;
    }

    public void setMapMakingFlag(Boolean mapMakingFlag) {
        this.mapMakingFlag = mapMakingFlag;
    }

    public boolean isHeatHome() {
        return heatHome;
    }

    public void setHeatHome(boolean heatHome) {
        this.heatHome = heatHome;
    }
}
