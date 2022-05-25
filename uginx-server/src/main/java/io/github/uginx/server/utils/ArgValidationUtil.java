package io.github.uginx.server.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.Random;

/**
 * @author wengchengjian
 * @date 2022/5/25-10:22
 */
public class ArgValidationUtil {

    private static final int[] PORT_RANGE = new int[]{5656,65000};


    public static boolean checkPortValidated(int port){
        return port>=PORT_RANGE[0] && port <= PORT_RANGE[1];
    }

    public static int getRandomPort(){
        return RandomUtil.randomInt(PORT_RANGE[0],PORT_RANGE[1]);
    }
}
