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

   

}
