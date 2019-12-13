

package com.example.tankwar.enums;

import com.example.tankwar.resource.map.*;


public enum LevelEnum {
    INVALID_LEVEL(-1, "无效", null),
    FIRST_LEVEL(1, "第一关", new Map1()),
    SECOND_LEVEL(2, "第二关", new Map2()),
    THIRD_LEVEL(3, "第三关", new Map3()),
    FOUR_LEVEL(4, "第四关", new Map4()),
    FIVE_LEVEL(5, "第五关", new Map5());

    private Integer level;
    private String name;
    private Map map;

    LevelEnum(Integer level, String name, Map map) {
        this.level = level;
        this.name = name;
        this.map = map;
    }

    public static LevelEnum getByLevel(Integer level) {
        for (LevelEnum levelEnum : LevelEnum.values()) {
            if (levelEnum.getLevel().equals(level)) {
                return levelEnum;
            }
        }
        return INVALID_LEVEL;
    }

    public Integer getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public Map getMap() {
        return map;
    }
}
