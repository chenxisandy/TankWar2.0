

package com.example.tankwar.enums;


/**
 * Class Description...
 *用于物体stuff种类
 * @author 朱孝曦
 */
public enum StuffTypeEnum {
    INVALID(-1, "无效"),
    BRICK(0, "砖块"),
    IRON(1, "铁块"),
    WATER(2, "水池"),
    TANK(3, "坦克"),
    MAP(4, "地图");

    private Integer key;
    private String name;

    StuffTypeEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static StuffTypeEnum getByKey(Integer key) {
        for (StuffTypeEnum tmp : StuffTypeEnum.values()) {
            if (tmp.getKey().equals(key)) {
                return tmp;
            }
        }
        //不存在就无效
        return INVALID;
    }

    public Integer getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
