package com.example.tankwar.service;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import javax.swing.JPanel;

import com.example.tankwar.constant.GameConstants;
import com.example.tankwar.context.GameContext;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.enums.StuffTypeEnum;
import com.example.tankwar.enums.TankTypeEnum;
import com.example.tankwar.entity.Bomb;
import com.example.tankwar.entity.Brick;
import com.example.tankwar.entity.Bullet;
import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.entity.Iron;
import com.example.tankwar.entity.MyTank;
import com.example.tankwar.entity.Stuff;
import com.example.tankwar.entity.Tank;
import com.example.tankwar.resource.TankGameImages;
import com.example.tankwar.entity.Water;
import com.example.tankwar.resource.map.Map;
import com.example.tankwar.view.panel.GamePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaintService {
    @Autowired
    private GameContext context;
    private Brick rightBrick = new Brick(700, 50);
    private Iron rightIron = new Iron(700, 50);
    private Water rightWater = new Water(700, 50);

    /**
     * 画出东西（包括坦克、障碍物。。）
     *
     * @param g     Graphics
     * @param stuff 东西对象
     * @param panel 被画的那个面板
     */
    public void drawStuff(Graphics g, Stuff stuff, JPanel panel) {
        switch (stuff.getType()) {
            //枚举的switch，有意思，不需要+StuffTypeEnum.TANK
            case TANK:
                Tank tank = (Tank) stuff;
                switch (stuff.getDirect()) { // 判断所朝的方向
                    case NORTH:
                        this.drawNorth(g, tank, panel);
                        break;
                    case SOUTH:
                        this.drawSouth(g, tank, panel);
                        break;
                    case WEST:
                        this.drawWest(g, tank, panel);
                        break;
                    case EAST:
                        this.drawEast(g, tank, panel);
                        break;
                }
                break;
            case BRICK:
                //通过key值得到图片
                g.drawImage(TankGameImages.stuffImg[StuffTypeEnum.BRICK.getKey()],
                        stuff.getX() - 10, stuff.getY() - 10, 20, 20, panel);
                break;
            case IRON:
                g.drawImage(TankGameImages.stuffImg[StuffTypeEnum.IRON.getKey()], stuff.getX() - 10,
                        stuff.getY() - 10, 20, 20, panel);
                break;
            case WATER:
                g.drawImage(TankGameImages.stuffImg[StuffTypeEnum.WATER.getKey()],
                        stuff.getX() - 10, stuff.getY() - 10, 20, 20, panel);
                break;
        }

    }

    /**
     * 画出爆炸
     *
     * @param g     Graphics
     * @param bombs 炸弹对象容器
     * @param panel 被画的那个面板
     */
    public void drawBomb(Graphics g, Vector<Bomb> bombs, JPanel panel) {
        for (Bomb bomb : bombs) {
            int l = bomb.getL();
            if (bomb.getLifeTime() > 24) { // 生命值21-25
                g.drawImage(TankGameImages.bomb[0], bomb.getX() - l / 2, bomb.getY()
                        - l / 2, l, l, panel);
            } else if (bomb.getLifeTime() > 18) { // 生命值16-20
                g.drawImage(TankGameImages.bomb[1], bomb.getX() - l / 2, bomb.getY()
                        - l / 2, l, l, panel);
            } else if (bomb.getLifeTime() > 12) { // 生命值11-15
                g.drawImage(TankGameImages.bomb[2], bomb.getX() - l / 2, bomb.getY()
                        - l / 2, l, l, panel);
            } else if (bomb.getLifeTime() > 6) { // 生命值6-10
                g.drawImage(TankGameImages.bomb[3], bomb.getX() - l / 2, bomb.getY()
                        - l / 2, l, l, panel);
            } else { // 生命值低于6
                g.drawImage(TankGameImages.bomb[4], bomb.getX() - l / 2, bomb.getY()
                        - l / 2, l, l, panel);
            }
            bomb.lifeDown(); // 生命随时间衰减
            if (bomb.getLifeTime() == 0) { // 该炸弹死亡
                bomb.setLive(false);
            }
        }
    }

    /**
     * 画出敌人坦克和子弹
     *
     * @param g       Graphics
     * @param enemies 敌人坦克容量
     * @param panel   被画的面板
     */
    public void drawEnemyTank(Graphics g, Vector<EnemyTank> enemies, JPanel panel) {
        for (EnemyTank enemy : enemies) {
            this.drawStuff(g, enemy, panel); // 画出敌人的坦克
            //然后画出子弹
            for (int j = 0; j < enemy.getBullets().size(); j++) {
                if (enemy.getBullets().get(j) != null) {
                    Bullet eb = enemy.getBullets().get(j);
                    g.drawImage(TankGameImages.bullet, eb.getX() - 2,
                            eb.getY() - 2, 4, 4, panel);
                }
            }
        }
    }

    /**
     * 画出我的坦克和子弹
     *
     * @param g       Graphics
     * @param myTanks 我的坦克容量
     * @param panel   被画的那个面板
     */
    public void drawMyTank(Graphics g, Vector<MyTank> myTanks, JPanel panel) {
        // 取出我的坦克
        for (MyTank myTank : myTanks) {
            this.drawStuff(g, myTank, panel); // 画出我的坦克
            for (int i = 0; i < myTank.getBullets().size(); i++) {
                if (myTank.getBullets().get(i) != null) {
                    Bullet b = myTank.getBullets().get(i);
                    g.drawImage(TankGameImages.bullet, b.getX() - 2,
                            b.getY() - 2, 4, 4, panel);
                }
            }
        }
    }

    /**
     * 画出地图
     *
     * @param g     Graphics
     * @param map   地图对象
     * @param panel 被画的那个面板
     */
    public void drawMap(Graphics g, Map map, JPanel panel) {
        Vector<Brick> bricks = map.getBricks();
        Vector<Iron> irons = map.getIrons();
        Vector<Water> waters = map.getWaters();
        for (int i = 0; i < bricks.size(); i++) {
            this.drawStuff(g, bricks.get(i), panel);
        }
        for (int i = 0; i < irons.size(); i++) {
            this.drawStuff(g, irons.get(i), panel);
        }
        for (int i = 0; i < waters.size(); i++) {
            this.drawStuff(g, waters.get(i), panel);
        }
        g.drawImage(TankGameImages.stuffImg[3], 300 - 20, 600, panel);
    }

    /**
     * 画出一个面朝北的坦克
     *
     * @param g     Graphics
     * @param tank  东西对象
     * @param panel 被画的那个面板
     */
    public void drawNorth(Graphics g, Tank tank, JPanel panel) {

        Image image;
        if (tank.getTankType() == TankTypeEnum.MY) {
            g.setColor(Color.green);
            image = TankGameImages.myTankImg[DirectionEnum.NORTH.getKey()];// 初始化图片
        } else {
            image = TankGameImages.enemyTankImg[DirectionEnum.NORTH.getKey()];
            g.setColor(Color.gray);
        }
        g.drawImage(image, tank.getX() - 20, tank.getY() - 20, 40, 40, panel);
        //血条
        g.fillRect(tank.getX() - 20, tank.getY() - 30, tank.getBlood() * (tank instanceof MyTank ? 4 : 20), 5);
    }

    /**
     * 画出一个方向朝南的坦克
     *
     * @param g     Graphics
     * @param tank  东西对象
     * @param panel 被画的那个面板
     */
    public void drawSouth(Graphics g, Tank tank, JPanel panel) {

        Image image;
        if (tank.getTankType() == TankTypeEnum.MY) {
            g.setColor(Color.green);
            image = TankGameImages.myTankImg[DirectionEnum.SOUTH.getKey()];// 初始化图片
        } else {
            image = TankGameImages.enemyTankImg[DirectionEnum.SOUTH.getKey()];
            g.setColor(Color.gray);
        }
        g.drawImage(image, tank.getX() - 20, tank.getY() - 20, 40, 40, panel);
        g.fillRect(tank.getX() - 20, tank.getY() - 30, tank.getBlood() * (tank instanceof MyTank ? 4 : 20), 5);
    }

    /**
     * 画出一个方向朝西的坦克
     *
     * @param g     Graphics
     * @param tank  东西对象
     * @param panel 被画的那个面板
     */
    public void drawWest(Graphics g, Tank tank, JPanel panel) {
        Image image;
        if (tank.getTankType() == TankTypeEnum.MY) {
            image = TankGameImages.myTankImg[DirectionEnum.WEST.getKey()];// 初始化图片
            g.setColor(Color.green);
        } else {
            image = TankGameImages.enemyTankImg[DirectionEnum.WEST.getKey()];
            g.setColor(Color.gray);
        }
        g.drawImage(image, tank.getX() - 20, tank.getY() - 20, 40, 40, panel);
        g.fillRect(tank.getX() - 20, tank.getY() - 30, tank.getBlood() * (tank instanceof MyTank ? 4 : 20), 5);
    }

    /**
     * 画出一个方向朝东的坦克
     *
     * @param g     Graphics
     * @param tank  东西对象
     * @param panel 被画的那个面板
     */
    public void drawEast(Graphics g, Tank tank, JPanel panel) {
      Image image;
        if (tank.getTankType() == TankTypeEnum.MY) {
            image = TankGameImages.myTankImg[DirectionEnum.EAST.getKey()];// 初始化图片
            g.setColor(Color.green);
        } else {
            image = TankGameImages.enemyTankImg[DirectionEnum.EAST.getKey()];
            g.setColor(Color.gray);
        }
        g.drawImage(image, tank.getX() - 20, tank.getY() - 20, 40, 40, panel);
        g.fillRect(tank.getX() - 20, tank.getY() - 30, tank.getBlood() * (tank instanceof MyTank ? 4 : 20), 5);
    }

    /**
     * 画出游戏右边的那个面板
     *
     * @param g   Graphics
     * @param tgp 游戏主要面板对象
     */
    public void drawRight(Graphics g, GamePanel tgp, RealTimeGameData data) {
        if (data.getMapMakingFlag().equals(Boolean.TRUE)) {
            //此功能目前已经放弃
            g.drawString("当前选中画笔（可按C键切换）", 620, 20);
            if (data.getCurrentStuff() == StuffTypeEnum.IRON) {
                drawStuff(g, rightIron, tgp);
            } else if (data.getCurrentStuff() == StuffTypeEnum.BRICK) {
                drawStuff(g, rightBrick, tgp);
            } else if (data.getCurrentStuff() == StuffTypeEnum.WATER) {
                drawStuff(g, rightWater, tgp);
            } else {
                g.drawString("橡皮擦", 680, 50);
            }

        } else {
            for (int i = 0; i < data.getEnemyTankNum(); i++) {
                if (i >= 4) {
                    g.drawImage(TankGameImages.enemyTankImg[DirectionEnum.NORTH.getKey()],
                            402 + 50 * i, 100, 40, 40, tgp);
                } else {
                    g.drawImage(TankGameImages.enemyTankImg[DirectionEnum.NORTH.getKey()],
                            602 + 50 * i, 20, 40, 40, tgp);
                }
            }
            for (int j = 0; j < data.getMyTankNum(); j++) {
                g.drawImage(TankGameImages.myTankImg[DirectionEnum.NORTH.getKey()], 602 + 50 * j,
                        400, 40, 40, tgp);
            }
            g.drawString("我的坦克子弹数目:" + data.getMyBulletNum(), 620, 500);
        }

    }


    //最终进行统一调用
    public void rePaintPanel(GamePanel panel, Graphics g) {

        RealTimeGameData data = context.getRealTimeGameData();
        if (data.isStart()) {
            g.setColor(Color.black);
            g.fillRect(0, 0, GameConstants.GAME_PANEL_WIDTH, GameConstants.GAME_PANEL_HEIGHT);
            g.fillRect(280, 600, 40, 40);
            this.drawMap(g, data.getMap(), panel);
            this.drawMyTank(g, data.getMyTanks(), panel); // 画出我的坦克（包括子弹）
            this.drawEnemyTank(g, data.getEnemies(), panel); // 画出敌人坦克（包括子弹）
            this.drawBomb(g, data.getBombs(), panel); // 画出爆炸
            this.drawRight(g, panel, data);

            if (data.getMyTankNum() == 0 || data.isHeatHome()) { // 如果我的坦克数量为0
                g.drawImage(TankGameImages.gameOver, 250, data.getDy(), 100,
                        100, panel);
            }

            if (data.getEnemyTankNum() == 0) { // 如果敌人坦克的数量为0
                g.drawImage(TankGameImages.gameWin, 250, data.getDy(), 100,
                        100, panel);
            }
            if (data.getDy() == 250) {
                g.fillRect(0, 0, 800, 600);
                g.setColor(Color.BLUE);
                if (data.getMyTankNum() == 0 || data.isHeatHome()) {
                    g.drawString("失败了！！！", 300, 220);
//                    if (data.isHeatHome()) {
//                        data.setStart(false);
//                    }
                } else {
                    g.drawString("挑战成功，请稍等...", 300, 220);
                }
                g.drawString(
                        ("敌人坦克死亡数量:" + (8 - data.getEnemyTankNum())),
                        300, 260);
                g.drawString("我的坦克死亡总数量:" + data.getBeKilled(), 300,
                        280);
                g.drawString(
                        "我的炮弹消耗总数量:"
                                + (GameConstants.MY_TANK_INIT_BULLET_NUM - data
                                .getMyBulletNum()), 300, 300);
                g.drawString("敌人坦克剩余数量:" + data.getEnemyTankNum(), 300,
                        320);
                g.drawString("我的坦克剩余总数量:" + data.getMyTankNum(), 300,
                        340);
                g.drawString("我的炮弹剩余总数量:" + data.getMyBulletNum(), 300,
                        360);
            }
        } else {
            g.drawImage(TankGameImages.startImage, 0, 0, 800, 700, panel);
//            g.drawImage(TankGameImages.font, 0, data.getKy(), panel);
            //制作笑的动态效果
            if (data.isIconSmile()) {
                g.drawImage(TankGameImages.yctSmile1, data.getKx(), 45,
                        panel);
                data.setIconSmile(false);
            } else {
                g.drawImage(TankGameImages.yctSmile2, data.getKx(), 45,
                        panel);
                data.setIconSmile(true);
            }
        }
    }
}
