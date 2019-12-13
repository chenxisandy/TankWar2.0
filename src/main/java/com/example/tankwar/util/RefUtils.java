
package com.example.tankwar.util;


import com.example.tankwar.exception.TankBattleGameException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 反射工具类.
 */
public class RefUtils {

    /**
     * 根据方法名执行方法
     *
     * @param obj            obj
     * @param methodName     methodName
     * @param parameterTypes parameterTypes
     * @param args           args
     * @return Object
     */
    public static Object executeByMethodName(Object obj, String methodName, Class<?>[]
            parameterTypes, Object[] args) {
        try {
            Method func = obj.getClass().getMethod(methodName, parameterTypes);
            return func.invoke(obj, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new TankBattleGameException("executeByMethodName exception");
        }
    }


    private RefUtils() {
        throw new IllegalStateException("Utility class");
    }
}
