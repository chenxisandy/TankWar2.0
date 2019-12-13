package com.example.tankwar.thread.executor;

import com.example.tankwar.context.GameContext;
import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.service.TankEventService;
import com.example.tankwar.service.GameEventService;
import com.example.tankwar.thread.task.EnemyTankAutoShotTask;
import com.example.tankwar.thread.task.EnemyTankMoveTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Vector;

/**
 * Class Description：负责执行线程
 */
@Component
public class TaskExecutor {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private GameContext gameContext;
    @Autowired
    private TankEventService enemyTankEventService;
    @Autowired
    private GameEventService gameEventService;

    public void startEnemyTankThreads() {
        Vector<EnemyTank> enemies = gameContext.getRealTimeGameData().getEnemies();
        enemies.forEach(e -> {
            taskExecutor.execute(new EnemyTankMoveTask(e, enemyTankEventService));
            e.getTimer().schedule(new EnemyTankAutoShotTask(e, gameEventService), 0, 500);
        });
    }

    public void startSingleEnemyTankTask(EnemyTank tank) {
        taskExecutor.execute(new EnemyTankMoveTask(tank, enemyTankEventService));
        tank.getTimer().schedule(new EnemyTankAutoShotTask(tank, gameEventService), 0, 500);
    }
}
