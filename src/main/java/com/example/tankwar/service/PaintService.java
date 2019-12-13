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

}
