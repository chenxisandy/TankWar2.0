package com.example.tankwar.service;

import java.util.Vector;

import com.example.tankwar.context.GameContext;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.enums.LevelEnum;
import com.example.tankwar.enums.StuffTypeEnum;
import com.example.tankwar.entity.Bomb;
import com.example.tankwar.entity.Brick;
import com.example.tankwar.entity.Bullet;
import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.entity.Iron;
import com.example.tankwar.entity.MyTank;
import com.example.tankwar.entity.Stuff;
import com.example.tankwar.entity.Tank;
import com.example.tankwar.entity.Water;
import com.example.tankwar.resource.map.Map;
import com.example.tankwar.thread.executor.TaskExecutor;
import com.example.tankwar.thread.task.BulletMoveTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * Control...
 *
 */
@Service
public class GameEventService {

    @Autowired
    private GameContext context;
    @Autowired
    private TaskExecutor threadTaskExecutor;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private TankEventService tankEventService;


    //子弹击中
    private Boolean isHitting(Bullet bullet, Stuff stuff) {
        return (Math.abs(bullet.getX() - stuff.getX()) <= (stuff.getWidth() + bullet.getWidth()) / 2 &&
                Math.abs(bullet.getY() - stuff.getY()) <= (stuff.getWidth() + bullet.getHeight()) / 2);
    }

    //子弹对消判断
    private Boolean isHitting(Bullet bullet1, Bullet bullet2) {
        return (Math.abs(bullet1.getX() - bullet2.getX()) <= bullet1.getWidth() &&
                Math.abs(bullet1.getY() - bullet2.getY()) <= bullet1.getHeight());
    }


    public void refreshState() {
        RealTimeGameData resource = context.getRealTimeGameData();
        Vector<EnemyTank> enemies = resource.getEnemies();
        Vector<MyTank> myTanks = resource.getMyTanks();

        //让敌方坦克冲我来
        if (!myTanks.isEmpty()) {
            enemies.forEach(enemyTank -> {
                enemyTank.setMyTankDirect(myTanks.get(0).getDirect());
            });
        }


    }

    /**
     * Bullets Event...
     */
    public void doBulletEvent() {
        RealTimeGameData resource = context.getRealTimeGameData();

        Vector<MyTank> myTanks = resource.getMyTanks();
        Vector<EnemyTank> enemies = resource.getEnemies();
        Vector<Bomb> bombs = resource.getBombs();
        Map map = resource.getMap();

        //死后停止射击
        if (myTanks.isEmpty()) {
            enemies.forEach(enemyTank -> enemyTank.setShot(false));
        }

        myTanks.forEach(myTank ->
                enemies.forEach(enemyTank -> {

                    //敌方ai处理
                    tankEventService.enemyFindAndKill(enemyTank, myTank, map);

                    //敌方射中我
                    enemyTank.getBullets().forEach(eb -> {
                        if (isHitting(eb, myTank)) {
                            this.afterShotTank(eb, myTank, bombs);
                        }

                        map.getBricks().stream().filter(brick -> isHitting(eb, brick))
                                .forEach(brick -> afterShotStuff(eb, brick, bombs, enemyTank));

                        map.getIrons().stream().filter(iron -> isHitting(eb, iron))
                                .forEach(iron -> afterShotStuff(eb, iron, bombs, enemyTank));
                    });

                    //子弹对射消除
                    myTank.getBullets().forEach(mb -> {
                        enemyTank.getBullets().stream().filter(eb -> isHitting(mb, eb))
                                .forEach(eb -> {
                                    mb.setLive(false);
                                    eb.setLive(false);
                                    Bomb bomb = new Bomb(mb.getX(), mb.getY());
                                    bomb.setL(20);
                                    bombs.add(bomb);
                                });

                        //我射中敌人

                        if (isHitting(mb, enemyTank)) {
                            this.afterShotTank(mb, enemyTank, bombs);
                        }

                        //射中砖块
                        map.getBricks().stream().filter(brick -> isHitting(mb, brick))
                                .forEach(brick -> afterShotStuff(mb, brick, bombs, myTank));

                        map.getIrons().stream().filter(iron -> isHitting(mb, iron))
                                .forEach(iron -> afterShotStuff(mb, iron, bombs, myTank));
                    });


                })
        );
    }

    /**
     * doOverlapJudge
     */
    public void doOverlapJudge() {
        RealTimeGameData resource = context.getRealTimeGameData();
        Vector<MyTank> myTanks = resource.getMyTanks();
        Vector<EnemyTank> enemies = resource.getEnemies();
        Map map = resource.getMap();
        Vector<Brick> bricks = map.getBricks();
        Vector<Iron> irons = map.getIrons();
        Vector<Water> waters = map.getWaters();

        myTanks.stream().forEach(myTank -> {
            myTank.setCanOverlap(false);
            myTank.setOverlapYes(false);

            if (tankEventService.isMyTankOverlap(myTank, enemies)) {
                myTank.setOverlapYes(true);
            }

            bricks.stream().filter(brick -> tankEventService.isTankOverlap(myTank, brick, 20 + 10))
                    .forEach(brick -> myTank.setOverlapYes(true));

            irons.stream().filter(iron -> tankEventService.isTankOverlap(myTank, iron, 20 + 10))
                    .forEach(iron -> myTank.setCanOverlap(true));

            waters.stream().filter(water -> tankEventService.isTankOverlap(myTank, water, 20 + 10))
                    .forEach(water -> myTank.setCanOverlap(true));
        });

        enemies.stream().forEach(enemyTank -> {
            enemyTank.setCanOverlap(false);
            enemyTank.setOverlapYes(false);
            enemyTank.setFrontStuff(StuffTypeEnum.INVALID);

            //重叠判断
            if (tankEventService.isEnemyTankOverlap(enemyTank, enemies, myTanks)) {
                enemyTank.setOverlapYes(true);
            }


            bricks.stream().filter(brick -> tankEventService.isTankOverlap(enemyTank, brick, 20 + 10))
                    .forEach(brick -> {
                        if ((Math.abs(brick.getX() - enemyTank.getX()) <= 10 && (enemyTank
                                .getDirect() == DirectionEnum.SOUTH || enemyTank
                                .getDirect() == DirectionEnum.NORTH))
                                || (Math.abs(brick.getY()
                                - enemyTank.getY()) <= 10 && (enemyTank
                                .getDirect() == DirectionEnum.EAST || enemyTank
                                .getDirect() == DirectionEnum.WEST))) {
                            enemyTank.setFrontStuff(StuffTypeEnum.BRICK);
                            enemyTank.setOverlapYes(true);
                            enemyTank.setShot(true);
                        } else {
                            enemyTank.setCanOverlap(true);
                        }

                    });

            irons.stream().filter(iron -> tankEventService.isTankOverlap(enemyTank, iron, 20 + 10))
                    .forEach(iron -> {
                        enemyTank.setFrontStuff(StuffTypeEnum.IRON);
                        enemyTank.setCanOverlap(true);
                    });

            waters.stream().filter(water -> tankEventService.isTankOverlap(enemyTank, water, 20 + 10))
                    .forEach(water -> {
                        enemyTank.setCanOverlap(true);
                        enemyTank.setCanOverlap(true);
                    });

        });


    }

    /**
     * cleanAndCreate
     */
    public void cleanAndCreate() {
        RealTimeGameData data = context.getRealTimeGameData();

        Vector<MyTank> myTanks = data.getMyTanks();
        Vector<EnemyTank> enemies = data.getEnemies();
        Vector<Bomb> bombs = data.getBombs();
        Map map = data.getMap();


//        for (MyTank myTank :
//                myTanks) {
//
//        }
        //此处不能用foreach表达式，因为语法糖不支持一些东西，比如taskExecutor
        //死后复活，直到坦克耗光
        for (int i = 0; i < myTanks.size(); i++) {
            MyTank myTank = myTanks.get(i);
            Vector<Bullet> mb = myTank.getBullets();
            mb.removeIf(b -> !b.isLive());

            if (!myTank.getLive()) {
                myTanks.remove(myTank);
                data.setMyTankNum(data.getMyTankNum() - 1);
                data.setBeKilled(data.getBeKilled() + 1);

                if (data.getMyTankNum() >= 1) {
                    // 1
                    MyTank myTankTemp = new MyTank(300, 620, DirectionEnum.NORTH);
                    myTanks.add(myTankTemp);
                }
            }
        }

        //敌方击中
        for (int i = 0; i < enemies.size(); i++) {
            EnemyTank enemy = enemies.get(i);
            Vector<Bullet> eb = enemy.getBullets();
            eb.removeIf(b -> !b.isLive());

            if (!enemy.getLive()) {
                enemy.getTimer().cancel();
                int r;


                data.setEnemyTankNum(data.getEnemyTankNum() - 1);
                r = (int) (Math.random() * 5);
                enemies.remove(enemy);
                //同屏敌方最大数量为5个
                if (data.getEnemyTankNum() >= 5) {
                    EnemyTank enemyTank = new EnemyTank((r) * 140 + 20,
                            -20, DirectionEnum.SOUTH);
                    enemyTank.setLocation(r);
                    enemyTank.setActivate(Boolean.TRUE);
                    threadTaskExecutor.startSingleEnemyTankTask(enemyTank);
                    enemies.add(enemyTank);
                }
                break;

            }
        }


        bombs.removeIf(bomb -> !bomb.isLive());

        map.getBricks().removeIf(brick -> !brick.getLive());

    }

    /**
     * 击中坦克以后
     *
     * @param bullet 击中别人的子弹
     * @param tank   被击中的坦克
     * @param bombs  炸弹容量
     */
    public void afterShotTank(Bullet bullet, Tank tank, Vector<Bomb> bombs) {
        bullet.setLive(false);
        Bomb bomb;
        if (tank.getBlood() == 1) {
            tank.setLive(false);
            bomb = new Bomb(tank.getX(), tank.getY());
            tank.setBlood(tank.getBlood() - 1);
            bomb.setL(120);
            bombs.add(bomb);
        } else {
            bomb = new Bomb(bullet.getX(), bullet.getY());
            tank.setBlood(tank.getBlood() - 1);
            bomb.setL(40);
            bombs.add(bomb);
        }
    }

    /**
     * 击中东西以后
     *
     * @param bullet 集中别人的子弹
     * @param stuff  被击中的东西
     * @param bombs  炸弹容量
     */
    public void afterShotStuff(Bullet bullet, Stuff stuff, Vector<Bomb> bombs,
                               Tank tank) {
        Bomb bomb;
        switch (stuff.getType()) {
            case BRICK: // 砖块
                bullet.setLive(false);
                stuff.setLive(false);
                bomb = new Bomb(stuff.getX(), stuff.getY());
                bomb.setL(40);
                bombs.add(bomb);
                break;
            case IRON: // 铁块
                bomb = new Bomb(bullet.getX(), bullet.getY());
                bullet.setLive(false);
                bomb.setL(20);
                bombs.add(bomb);
        }
    }

    /**
     * 我的坦克事件，观察我按了什么键
     * @param resource .getMyTanks() 我的坦克容量
     */
    public void myTankEvent(RealTimeGameData resource) {
        RealTimeGameData data = context.getRealTimeGameData();
        for (int i = 0; i < resource.getMyTanks().size(); i++) {
            MyTank myTank = resource.getMyTanks().get(i);
            //必须该方向没有阻挡才能去往该方向
            if (data.isUp() && !myTank.isCanOverlap() && !myTank.isOverlapYes()) {
                myTank.goNorth();
            } else if (data.isDown() && !myTank.isCanOverlap() && !myTank.isOverlapYes()) {
                myTank.goSouth();
            } else if (data.isLeft() && !myTank.isCanOverlap() && !myTank.isOverlapYes()) {
                myTank.goWest();
            } else if (data.isRight() && !myTank.isCanOverlap() && !myTank.isOverlapYes()) {
                myTank.goEast();
            }
        }
    }

    /**
     * 下一关
     */
    public void nextGame(RealTimeGameData resource) {
        RealTimeGameData data = context.getRealTimeGameData();

        resource.setMap(LevelEnum.getByLevel(data.getLevel()).getMap());

        for (int i = 0; i < 5; i++) {
            EnemyTank enemy = new EnemyTank((i) * 140 + 20, -20, DirectionEnum.SOUTH);
            enemy.setActivate(Boolean.TRUE);
            enemy.setLocation(i);
            resource.getEnemies().add(enemy);
        }
        data.setEnemyTankNum(8);
        for (int i = 0; i < resource.getMyTanks().size(); i++) {
            MyTank myTank = resource.getMyTanks().get(i);
            myTank.setActivate(Boolean.TRUE);
            myTank.setDirect(DirectionEnum.NORTH);
            myTank.setX(300);
            myTank.setY(620);
        }
        threadTaskExecutor.startEnemyTankThreads();
    }


    /**
     * 游戏暂停
     *
     * @param resource .getEnemies() 敌人坦克容量
     */
    public void gameEventStop(RealTimeGameData resource) {
        RealTimeGameData data = context.getRealTimeGameData();

        for (int i = 0; i < resource.getMyTanks().size(); i++) {
            MyTank myTank = resource.getMyTanks().get(i);
            if (myTank.getSpeedVector() == 0) {
                data.setStop(true);
                myTank.setSpeedVector(myTank.getSpeed());
                myTank.setSpeed(0);
                for (int j = 0; j < myTank.getBullets().size(); j++) {
                    myTank.getBullets()
                            .get(j)
                            .setSpeedVector(
                                    myTank.getBullets().get(j).getSpeed());
                    myTank.getBullets().get(j).setSpeed(0);
                }
                for (int j = 0; j < resource.getEnemies().size(); j++) {
                    resource.getEnemies()
                            .get(j)
                            .setSpeedVector(
                                    resource.getEnemies().get(j).getSpeed());
                    resource.getEnemies().get(j).setSpeed(0);
                    for (int k = 0; k < resource.getEnemies().get(j)
                            .getBullets().size(); k++) {
                        resource.getEnemies()
                                .get(j)
                                .getBullets()
                                .get(k)
                                .setSpeedVector(
                                        resource.getEnemies().get(j)
                                                .getBullets().get(k).getSpeed());
                        resource.getEnemies().get(j).getBullets().get(k)
                                .setSpeed(0);
                    }
                }
            } else {
                data.setStop(false);
                myTank.setSpeed(myTank.getSpeedVector());
                myTank.setSpeedVector(0);
                for (int j = 0; j < myTank.getBullets().size(); j++) {
                    myTank.getBullets()
                            .get(j)
                            .setSpeed(
                                    myTank.getBullets().get(j).getSpeedVector());
                }
                for (int j = 0; j < resource.getEnemies().size(); j++) {
                    resource.getEnemies()
                            .get(j)
                            .setSpeed(
                                    resource.getEnemies().get(j)
                                            .getSpeedVector());
                    resource.getEnemies().get(j).setSpeedVector(0);
                    for (int k = 0; k < resource.getEnemies().get(j)
                            .getBullets().size(); k++) {
                        resource.getEnemies()
                                .get(j)
                                .getBullets()
                                .get(k)
                                .setSpeed(
                                        resource.getEnemies().get(j)
                                                .getBullets().get(k)
                                                .getSpeedVector());
                    }
                }
            }
        }
    }



    /**
     * 射击，发射一颗子弹
     *
     * @param tank tank为射击者，而非被射击者
     */
    public void shot(Tank tank) {
        Bullet bullet = null;
        switch (tank.getDirect()) {
            case NORTH:
                bullet = new Bullet(tank.getX(), tank.getY() - 20, DirectionEnum.NORTH);
                break;
            case SOUTH:
                bullet = new Bullet(tank.getX(), tank.getY() + 20, DirectionEnum.SOUTH);
                break;
            case WEST:
                bullet = new Bullet(tank.getX() - 20, tank.getY(), DirectionEnum.WEST);
                break;
            case EAST:
                bullet = new Bullet(tank.getX() + 20, tank.getY(), DirectionEnum.EAST);
                break;
        }
        tank.getBullets().add(bullet);
        taskExecutor.execute(new BulletMoveTask(bullet));
    }


}
