package io.github.uginx.client.netty;

import io.netty.channel.Channel;

/**
 * @author wengchengjian
 * @date 2022/5/25-15:53
 */
public class ProxyClientChannelContextHolder {

    private static  Channel channel;

    public static Channel getChannel(){
        return channel;
    }
    public static synchronized void setChannel(Channel clientChannel){
        channel = clientChannel;
    }
}
