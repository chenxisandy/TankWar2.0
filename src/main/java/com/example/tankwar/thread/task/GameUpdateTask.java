package com.example.tankwar.thread.task;

import com.example.tankwar.context.GameContext;
import com.example.tankwar.service.GameEventService;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.thread.GameTimeUnit;
import com.example.tankwar.view.panel.GamePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UpdateThread...
 *
 * 游戏运行线程
 */
public class GameUpdateTask implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GameContext gameContext;


    public GameUpdateTask(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void run() {
        GamePanel panel = gameContext.getGamePanel();
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        GameEventService control = gameContext.getControl();
        // 每隔30毫秒重画
        while (true) {
            GameTimeUnit.sleepMillis(30);
            if (gameData.isStart()) {
                if ((gameData.getMyTankNum() == 0 || gameData.getEnemyTankNum() == 0)
                        && gameData.getDy() > 250) {
                    gameData.setDy(gameData.getDy() - 2);
                }
                if (gameData.getDy() == 250) {
                    panel.repaint();
                    GameTimeUnit.sleepMillis(4000);
                    if (gameData.getLevel() == 5) {
                        gameData.setLevel(0);
                    }
                    if (gameData.getMyTankNum() >= 1 && gameData.getLevel() <= 4) {
                        gameData.setLevel(gameData.getLevel() + 1);
                        gameData.setDy(600);
                        control.nextGame(gameData);
                    }
                }
                if (!gameData.isStop() && gameData.getDy() == 600) {
                    control.cleanAndCreate(); // 从容器中移除死亡的对象
                    control.refreshState();
                    control.doBulletEvent();
                    control.doOverlapJudge(); // 判断坦克间是否出现重叠
                    control.myTankEvent(gameData);

                }
            } else {
                if (gameData.getKy() == 21 && gameData.getKx() <= 654) {
                    gameData.setKx(gameData.getKx() + 2);
                }
                //control.fontMove(panel);
                GameTimeUnit.sleepMillis(100);

            }
            panel.repaint();
            logger.debug("data : {}", gameData);
        }
    }


}
