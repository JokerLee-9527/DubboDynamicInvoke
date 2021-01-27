package com.joker.dubbo.dynamic.invoke.util;

import java.util.Properties;

/**
 * @author JokerLee
 * @version V1.0
 * @Description: 操作系统相关的通用方法
 * @date 2021/1/4 17:53
 */
public class OsUtil {

    /**
     * 判断是否是linux
     *
     * @return
     */
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是Mac
     *
     * @return
     */
    public static boolean isOSMac() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("mac")) {
            return true;
        } else {
            return false;
        }
    }
}
