package io.github.uginx.server.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wengchengjian
 * @date 2022/5/25-13:13
 */
public class ProxyClientChannelManager {

    /**
     * key:clientKey
     * value:Channel
     */
    private static final Map<String, Channel> CLIENT_CHANNEL = new ConcurrentHashMap<>(64);

    private static final Map<String,Map<String,Channel>> CLIENT_PROXY_CHANNEL = new ConcurrentHashMap<>(64);

    public static Channel getClientProxyChannel(String clientKey,String host,Integer port){
        return CLIENT_PROXY_CHANNEL.get(clientKey).get(getAddress(host,port));
    }

    public static Channel getClientProxyChannel(String clientKey,String address){
        return CLIENT_PROXY_CHANNEL.get(clientKey).get(address);
    }

    public static void addClientProxyChannel(String clientKey,String host,Integer port,Channel channel){
        Map<String, Channel> proxyChannelMap = CLIENT_PROXY_CHANNEL.get(clientKey);
        String address = getAddress(host, port);

        if(proxyChannelMap==null){
            proxyChannelMap = new ConcurrentHashMap<>(16);
            CLIENT_PROXY_CHANNEL.put(address,proxyChannelMap);
        }
        proxyChannelMap.put(address,channel);

    }

    public static String getAddress(String host,Integer port){
        return host +":"+ String.valueOf(port);
    }

    public static boolean containsClientKey(String clientKey){
        return CLIENT_CHANNEL.containsKey(clientKey);
    }

    public static Channel getClientChannel(String clientKey){
        return CLIENT_CHANNEL.get(clientKey);
    }

    public static void addClientChannel(String clientKey, Channel channel){
        CLIENT_CHANNEL.put(clientKey,channel);
    }
}
