package com.example.tankwar.service;

import com.example.tankwar.entity.*;
import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.resource.map.Map;
import com.example.tankwar.thread.GameTimeUnit;
import org.springframework.stereotype.Service;

import java.util.Vector;

/**
 * Class Description...
 */
@Service
public class TankEventService {

    /**
     * 判断坦克是否与另一个事物重叠
     *
     * @param stuff  东西对象
     * @param length 两者之间的最短距离
     * @return 是否重叠
     */
    public boolean isTankOverlap(Tank tank, Stuff stuff, int length) {
        boolean isOverlap = false;
        int x = stuff.getX();
        int y = stuff.getY();
        if (tank.getDirect() == DirectionEnum.NORTH) {
            //进行预判
            tank.setY(tank.getY() - tank.getSpeed());
            if (Math.abs(tank.getY() - y) < length
                    && Math.abs(tank.getX() - x) < length) {
                isOverlap = true;
            }
            //预判完成就还原
            tank.setY(tank.getY() + tank.getSpeed());
        }
        if (tank.getDirect() == DirectionEnum.SOUTH) {
            tank.setY(tank.getY() + tank.getSpeed());
            if (Math.abs(tank.getY() - y) < length
                    && Math.abs(tank.getX() - x) < length) {
                isOverlap = true;
            }
            tank.setY(tank.getY() - tank.getSpeed());
        }
        if (tank.getDirect() == DirectionEnum.EAST) {
            tank.setX(tank.getX() + tank.getSpeed());
            if (Math.abs(tank.getY() - y) < length
                    && Math.abs(tank.getX() - x) < length) {
                isOverlap = true;
            }
            tank.setX(tank.getX() - tank.getSpeed());
        }
        if (tank.getDirect() == DirectionEnum.WEST) {
            tank.setX(tank.getX() - tank.getSpeed());
            if (Math.abs(tank.getY() - y) < length
                    && Math.abs(tank.getX() - x) < length) {
                isOverlap = true;
            }
            tank.setX(tank.getX() + tank.getSpeed());
        }
        return isOverlap;
    }

    /**
     * 判断是否重叠
     *
     * @param enemies 敌人坦克容量
     * @return 是否重叠
     */
    public boolean isMyTankOverlap(MyTank tank, Vector<EnemyTank> enemies) {
        for (EnemyTank enemy : enemies) {
            if (isTankOverlap(tank, enemy, 40))
                return true;
        }
        return false;
    }

    /**
     * 判断自己跟别的坦克是否重叠
     *
     * @param enemies 敌人坦克容量
     * @param myTanks 我的坦克容量
     * @return 是否重叠
     */
    public boolean isEnemyTankOverlap(EnemyTank enemy, Vector<EnemyTank> enemies, Vector<MyTank> myTanks) {
        for (EnemyTank enemyTank : enemies) {
            if (enemy != enemyTank) {
                if (isTankOverlap(enemy, enemyTank, 40)) {
                    enemy.setCanOverlap(true);
                    return true;
                }
            }
        }
        for (MyTank myTank : myTanks) {
            if (isTankOverlap(enemy, myTank, 40)) {
                enemy.setOverlapYes(true);
                return true;
            }
        }

        enemy.setCanOverlap(false);
        enemy.setOverlapYes(false);
        return false;
    }

    //敌方朝我走
    /**
     * 每隔36毫秒 一直向西走
     */
    public void enemyGoWest(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isCanOverlap() && !enemy.isOverlapYes()) {
                enemy.goWest();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.WEST) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向东走
     */
    public void enemyGoEast(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isCanOverlap() && !enemy.isOverlapYes()) {
                enemy.goEast();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.EAST) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向北走
     */
    public void enemyGoNorth(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isCanOverlap() && !enemy.isOverlapYes()) {
                enemy.goNorth();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.NORTH) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 每隔36毫秒 一直向南走
     */
    public void enemyGoSouth(EnemyTank enemy) {
        for (; ; ) {
            GameTimeUnit.sleepMillis(36);
            if (!enemy.isCanOverlap() && !enemy.isOverlapYes()) {
                enemy.goSouth();
            }
            if (enemy.getMyTankLocation() != DirectionEnum.SOUTH) {
                enemy.setDirect(enemy.getMyTankDirect());
                break;
            }
        }
    }

    /**
     * 从指定的三个方向中随机选择一个
     *
     * @param direct1 方向1
     * @param direct2 方向2
     * @param direct3 方向3
     */
    public DirectionEnum enemyGetRandomDirect(DirectionEnum direct1, DirectionEnum direct2, DirectionEnum direct3) {
        int random = (int) (Math.random() * 3);

        DirectionEnum returnDirect = DirectionEnum.INVALID;
        switch (random) {
            case 0:
                returnDirect = direct1;
                break;
            case 1:
                returnDirect = direct2;
                break;
            case 2:
                returnDirect = direct3;
                break;
        }
        return returnDirect;
    }

    /**
     * 让敌人坦克能够发现我的坦克并开炮
     * 敌方ai
     *
     * @param myTank 我的坦克
     * @param map    地图对象
     */
    public void enemyFindAndKill(EnemyTank enemy, MyTank myTank, Map map) {
        int myX = myTank.getX();
        int myY = myTank.getY();
        int enX = enemy.getX();
        int enY = enemy.getY();
        //距离小于20并且我已出基地且感觉良好
        if (Math.abs(myX - enX) < 20 && myY <= 580) {
            if (enY < myY) {
                int s = 0;
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    //2人中间有铁块就不开火
                    if (Math.abs(enX - iron.getX()) <= 10 && iron.getY() > enY
                            && iron.getY() < myY) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.SOUTH);
                }
            } else {
                int s = 0;
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enX - iron.getX()) <= 10 && iron.getY() < enY
                            && iron.getY() > myY) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.NORTH);
                }
            }
        } else if (Math.abs(myY - enY) < 20 && myY <= 580) {
            if (enX > myX) {
                int s = 0;
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enY - iron.getY()) <= 10 && iron.getX() < enX
                            && iron.getX() > myX) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.WEST);
                }
            } else {
                int s = 0;
                for (int t = 0; t < map.getIrons().size(); t++) {
                    Iron iron = map.getIrons().get(t);
                    if (Math.abs(enY - iron.getY()) <= 10 && iron.getX() > enX
                            && iron.getX() < myX) {
                        s = 1;
                        break;
                    }
                }
                if (s == 0) {
                    enemy.setShot(true);
                    enemy.setMyTankLocation(DirectionEnum.EAST);
                }
            }
        } else {
            enemy.setShot(false);
            enemy.setMyTankLocation(DirectionEnum.INVALID);
        }
    }
}
