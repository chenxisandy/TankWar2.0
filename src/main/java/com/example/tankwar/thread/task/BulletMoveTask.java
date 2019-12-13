package com.example.tankwar.thread.task;


import com.example.tankwar.constant.GameConstants;
import com.example.tankwar.entity.Bullet;
import com.example.tankwar.thread.GameTimeUnit;

/**
 * Class Description...
 */
public class BulletMoveTask implements Runnable {
    private Bullet bullet;

    public BulletMoveTask(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void run() {
        while (bullet.isLive()) {
            switch (bullet.getDirect()) { // 选择子弹的方向
                case NORTH:
                    bullet.setY(bullet.getY() - bullet.getSpeed());
                    break;
                case SOUTH:
                    bullet.setY(bullet.getY() + bullet.getSpeed());
                    break;
                case WEST:
                    bullet.setX(bullet.getX() - bullet.getSpeed());
                    break;
                case EAST:
                    bullet.setX(bullet.getX() + bullet.getSpeed());
                    break;
            }

            if (bullet.getX() < 5 || bullet.getX() > GameConstants.GAME_PANEL_WIDTH - 5 || bullet.getY() < 5
                    || bullet.getY() > GameConstants.GAME_PANEL_HEIGHT - 5) { // 判断子弹是否碰到边界
                bullet.setLive(false); // 子弹死亡
            }

            GameTimeUnit.sleepMillis(36);
        }
    }
}
