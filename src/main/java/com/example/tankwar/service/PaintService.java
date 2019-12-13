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
}
