package com.cw.test;


import java.lang.reflect.Method;

public class Utils {
    public static long getCurrentSec() {
        return getCurrentMS()/1000;
    }

    public static long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public static boolean invokeReflectionMethod(RelectionMethod relectionMethod)  {
        Method method = null;
        try {
            method = relectionMethod.getClass().getMethod("getMethod");
            method.setAccessible(true);
            method.invoke(relectionMethod,null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
