package com.example.tankwar.service;

import com.example.tankwar.context.GameContext;
import com.example.tankwar.dto.RealTimeGameData;
import com.example.tankwar.resource.map.Map;
import com.example.tankwar.resource.map.xml.parse.MapParser;
import com.example.tankwar.util.RefUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;

/**
 * Class Description...
 */
@Service
public class CommandService {
    @Autowired
    private GameEventService gameEventService;

    @Autowired
    private GameContext gameContext;

    public void executeByCmd(String cmd) {
        RefUtils.executeByMethodName(this, cmd, null, null);
//        switch (cmd) {
//            case ActionEvent
//        }
    }

    public void executeCustomMapMenu(String para) {
        RefUtils.executeByMethodName(this, "loadCustomMap", new Class[]{String.class}, new String[]{para});
    }

    public void loadCustomMap(String mapName){
        System.out.println(mapName);
        gameContext.getRealTimeGameData().setMap(new Map(MapParser.getMapFromXml(mapName)));
        gameContext.startGame();
    }

    public void stop() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        gameEventService.gameEventStop(gameData);
    }

    public void start() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        if (!gameData.isStart()) { // 还没开始
            gameContext.startGame();// 已经开始了
            //this.setVisible(true);
        } else if (!gameData.isStop()
                && gameData.getMyTankNum() != 0) {
            // 暂停
            gameEventService.gameEventStop(gameData);
            JOptionPane.showMessageDialog(null, "游戏已经开始", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            // 恢复游戏
            gameEventService.gameEventStop(gameData);
        } else if (gameData.isStop()) {
            JOptionPane.showMessageDialog(null, "游戏已经开始", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (!gameData.isStop()
                && (gameData.getMyTankNum() == 0)) {
            gameContext.startGame();
        }
    }

    public void exit() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        // 暂停游戏
        gameEventService.gameEventStop(gameData);
        int select = JOptionPane.showConfirmDialog(null, "退出游戏吗？", "退出确认",
                JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.OK_OPTION) {
            // 退出游戏
            System.exit(0);
        } else {
            // 恢复游戏
            gameEventService.gameEventStop(gameData);
        }
    }

    public void again() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        if (!gameData.isStop()) {
            gameEventService.gameEventStop(gameData);
        }
            int select = JOptionPane.showConfirmDialog(null, "确认重来", "选择确认", JOptionPane.OK_CANCEL_OPTION);
            if (select == JOptionPane.OK_OPTION) {
                gameEventService.gameEventStop(gameData);
                gameContext.startLevel(gameData.getLevel());
            } else {
                gameEventService.gameEventStop(gameData);
            }

    }

    public void first() {
        selectLevel(1);
    }

    public void second() {
        selectLevel(2);
    }

    public void third() {
        selectLevel(3);
    }

    public void fourth() {
        selectLevel(4);
    }

    public void fifth() {
        selectLevel(5);
    }

    public void selectLevel(int level) {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        if (gameData.isStart()) {
            if (!gameData.isStop()) {// 暂停游戏
                gameEventService.gameEventStop(gameData);
            }
            int select = JOptionPane.showConfirmDialog(null,
                    "您选择的是第" + level + "关，点击确定开始游戏", "选择确认",
                    JOptionPane.OK_CANCEL_OPTION);
            if (select == JOptionPane.OK_OPTION) {
                if (gameData.isStart()) {
                    // 恢复游戏
                    gameEventService.gameEventStop(gameData);
                }
                gameContext.startLevel(level);
            } else {
                if (gameData.isStart()) {
                    // 恢复游戏
                    gameEventService.gameEventStop(gameData);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "请先点击游戏Game菜单下的开始游戏\n然后才能在游戏中选择关卡", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void help() {
        JOptionPane.showMessageDialog(
                null,
                "坦克移动:方向键\n坦克开炮:J键\n每关只要打掉敌人8辆坦克即可过关，\n",
                "游戏帮助", JOptionPane.INFORMATION_MESSAGE);
    }

    public void about() {
        JOptionPane.showMessageDialog(null,
                "JAVA坦克大战2.0\nfor 2019 java 课设\n", "关于游戏",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void createMap() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        gameData.setMapMakingFlag(Boolean.TRUE);
        gameData.getEnemies().forEach(t -> t.setLive(Boolean.FALSE));
        gameData.getMyTanks().forEach(t -> t.setLive(Boolean.FALSE));
        gameData.getMyTanks().clear();
        gameData.getEnemies().clear();
        gameData.setMap(new Map());
        gameData.setStart(Boolean.TRUE);
//        JOptionPane.showMessageDialog(null,
//                "该功能有缺陷故舍弃\n", "创建地图",
//                JOptionPane.INFORMATION_MESSAGE);
    }

    public void saveMap() {
        RealTimeGameData gameData = gameContext.getRealTimeGameData();
        String inputValue = JOptionPane.showInputDialog("请输入自定义地图名称");
        System.out.println(inputValue);
        try {
            MapParser.generateXmlFromMap(gameData.getMap(), inputValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JOptionPane.showMessageDialog(null,
//                "该功能有缺陷故舍弃\n", "保存地图",
//                JOptionPane.INFORMATION_MESSAGE);
    }
}
