package com.example.tankwar.listener;

import com.example.tankwar.context.GameContext;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.enums.StuffTypeEnum;
import com.example.tankwar.service.GameEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class MainFrameKeyListener implements KeyListener {
    @Autowired
    private GameEventService gameEventService;

    @Autowired
    private GameContext gameContext;

    @Override
    public void keyPressed(KeyEvent e) {
        RealTimeGameData data = gameContext.getRealTimeGameData();



        if (data.getMapMakingFlag() && e.getKeyCode() == KeyEvent.VK_C) {
            if (data.getCurrentStuff() == StuffTypeEnum.BRICK) {
                data.setCurrentStuff(StuffTypeEnum.IRON);
            } else if (data.getCurrentStuff() == StuffTypeEnum.IRON) {
                data.setCurrentStuff(StuffTypeEnum.WATER);
            } else if (data.getCurrentStuff() == StuffTypeEnum.WATER) {
                data.setCurrentStuff(StuffTypeEnum.INVALID);
            } else {
                data.setCurrentStuff(StuffTypeEnum.BRICK);
            }

        }


        if (e.getKeyCode() == KeyEvent.VK_P) { // 暂停
            gameEventService.gameEventStop(data);
        }

        data.getMyTanks().forEach(myTank -> {
            if (!myTank.getLive()) {
                data.keyPressedDirect(false, false, false, false);
            } else {
                if ((e.getKeyCode() == KeyEvent.VK_UP)) {
                    myTank.setDirect(DirectionEnum.NORTH);
                    data.keyPressedDirect(true, false, false, false);

                } else if ((e.getKeyCode() == KeyEvent.VK_DOWN) && myTank.getY() <= 580) {
                    myTank.setDirect(DirectionEnum.SOUTH);
                    data.keyPressedDirect(false, true, false, false);

                } else if ((e.getKeyCode() == KeyEvent.VK_LEFT) && myTank.getY() <= 580) {
                    myTank.setDirect(DirectionEnum.WEST);
                    data.keyPressedDirect(false, false, true, false);

                } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && myTank.getY() <= 580) {
                    myTank.setDirect(DirectionEnum.EAST);
                    data.keyPressedDirect(false, false, false, true);

                }
                if (e.getKeyCode() == KeyEvent.VK_J && myTank.getY() <= 580) {
                    if (myTank.getBullets().size() <= 1 && data.getMyBulletNum() > 0) {
                        data.setMyBulletNum(data.getMyBulletNum() - 1);
                        gameEventService.shot(myTank);
                    }
                }
            }
        });

    }

    @Override
    public void keyReleased(KeyEvent e) {
        RealTimeGameData data = gameContext.getRealTimeGameData();
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            data.setUp(false);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            data.setDown(false);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            data.setLeft(false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            data.setRight(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
