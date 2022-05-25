package io.github.uginx.server.utils;

/**
 * @author wengchengjian
 * @date 2022/5/25-10:22
 */
public class ArgValidationUtil {

    private static final int[] PORT_RANGE = new int[]{5656,65000};

    public static boolean checkPortValidated(int port){
        return port>=PORT_RANGE[0] && port <= PORT_RANGE[1];
    }
}
