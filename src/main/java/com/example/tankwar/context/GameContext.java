package com.example.tankwar.context;

import com.example.tankwar.constant.GameConstants;
import com.example.tankwar.enums.DirectionEnum;
import com.example.tankwar.listener.MainFrameMouseListener;
import com.example.tankwar.service.GameEventService;
import com.example.tankwar.service.PaintService;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.enums.LevelEnum;
import com.example.tankwar.entity.EnemyTank;
import com.example.tankwar.entity.MyTank;
import com.example.tankwar.thread.executor.TaskExecutor;
import com.example.tankwar.thread.task.GameUpdateTask;
import com.example.tankwar.listener.MainFrameKeyListener;
import com.example.tankwar.listener.MenuActionListener;
import com.example.tankwar.util.LogUtils;
import com.example.tankwar.view.frame.GameFrame;
import com.example.tankwar.view.menubar.TankBattleMenuBar;
import com.example.tankwar.view.panel.GamePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class GameContext {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Frame
     */
    private GameFrame gameFrame;
    /**
     * 菜单条
     */
    private TankBattleMenuBar tankBattleMenuBar;
    /**
     * Panel
     */
    private GamePanel gamePanel;
    /**
     * RealTimeGameData
     */
    private RealTimeGameData realTimeGameData;

    //名称匹配，自动装配
    @Autowired
    private MainFrameKeyListener mainFrameKeyListener;
    @Autowired
    private MenuActionListener menuActionListener;
    @Autowired
    private MainFrameMouseListener mainFrameMouseListener;
    @Autowired
    private GameEventService control;
    @Autowired
    private PaintService paintService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private TaskExecutor threadTaskExecutor;

    //初始的入口
    @EventListener
    public void init(ApplicationReadyEvent applicationStartedEvent) {
        LogUtils.info("Application Started... applicationStartedEvent={}", applicationStartedEvent);

        //初始化第一关
        initGameData(1);

        this.gameFrame = new GameFrame();
        this.tankBattleMenuBar = new TankBattleMenuBar(menuActionListener);
        this.gamePanel = new GamePanel(paintService);

        this.gameFrame.setJMenuBar(this.tankBattleMenuBar);
        this.gameFrame.add(this.gamePanel);
        this.gameFrame.addKeyListener(mainFrameKeyListener);
        this.gamePanel.addMouseListener(mainFrameMouseListener);

        this.gameFrame.setVisible(true);

        logger.info("execute UpdateTask...");
        taskExecutor.execute(new GameUpdateTask(this));
        logger.info("game start success...");
    }


    /**
     * 初始化指定关卡游戏数据
     *
     * @param level 关卡
     */
    private void initGameData(int level) {
        realTimeGameData = new RealTimeGameData();

        //敌方坦克生成
        for (int i = 0; i < GameConstants.INIT_ENEMY_TANK_IN_MAP_NUM; i++) {
            EnemyTank enemy = new EnemyTank((i) * 140 + 20, -20, DirectionEnum.SOUTH);
            enemy.setLocation(i);
            realTimeGameData.getEnemies().add(enemy);
        }
        for (int i = 0; i < 1; i++) {
            MyTank myTank = new MyTank(300, 620, DirectionEnum.NORTH);
            realTimeGameData.getMyTanks().add(myTank);
        }

        realTimeGameData.setMap(LevelEnum.getByLevel(level).getMap());
        //realTimeGameData.setMap(new Map(MapParser.getMapFromXml()));
        realTimeGameData.setEnemyTankNum(GameConstants.INIT_ENEMY_TANK_NUM);
        realTimeGameData.setMyTankNum(GameConstants.INIT_MY_TANK_NUM);
        realTimeGameData.setMyBulletNum(GameConstants.MY_TANK_INIT_BULLET_NUM);
        realTimeGameData.setBeKilled(0);
        realTimeGameData.setDy(600);
        realTimeGameData.setLevel(level);
        threadTaskExecutor.startEnemyTankThreads();
        logger.info("init Game Data end...");
    }


    private void reset(int level) {
        realTimeGameData.reset();
        initGameData(level);
        logger.info("init Game Data...");
    }


    public void startGame() {
        realTimeGameData.setStart(true);
        realTimeGameData.getEnemies().forEach(t -> t.setActivate(true));
        realTimeGameData.getMyTanks().forEach(t -> t.setActivate(true));
    }

    public void startLevel(int level) {
        reset(level);
        taskExecutor.execute(new GameUpdateTask(this));
        this.startGame();
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public TankBattleMenuBar getTankBattleMenuBar() {
        return tankBattleMenuBar;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public RealTimeGameData getRealTimeGameData() {
        return realTimeGameData;
    }

    public GameEventService getControl() {
        return control;
    }

}
