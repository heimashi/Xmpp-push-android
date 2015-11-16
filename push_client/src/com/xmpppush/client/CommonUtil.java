package com.xmpppush.client;

public class CommonUtil {
    
    public static String makeLogTag(Class<?> cls) {
        return "xmpppush_" + cls.getSimpleName();
    }

}
