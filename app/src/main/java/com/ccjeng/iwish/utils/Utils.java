package com.ccjeng.iwish.utils;

import java.util.UUID;

/**
 * Created by andycheng on 2016/3/26.
 */
public class Utils {

    public static int gridColumnNum = 3;

    public static String getUniqueID() {
        return UUID.randomUUID().toString();
    }
}
