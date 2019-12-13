package com.example.tankwar.thread.task;

import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.service.GameEventService;

import java.util.TimerTask;

/**
 * MyTimerTask...
 */
public class EnemyTankAutoShotTask extends TimerTask {
    EnemyTank tank;
    GameEventService gameEventService;

    public EnemyTankAutoShotTask(EnemyTank tank, GameEventService gameEventService) {
        this.tank = tank;
        this.gameEventService = gameEventService;
    }

    @Override
    public void run() {
        if (tank.getSpeedVector() == 0 && tank.isShot() && tank.activate()) {
            gameEventService.shot(tank);
        }

    }

}