package io.github.uginx.client.utils;

import io.github.uginx.client.config.ClientProxyProperties;
import io.github.uginx.client.netty.ProxyClientChannelContextHolder;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.model.ProxyRequest;
import io.github.uginx.core.protocol.message.Message;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/5/25 22:03
 * @Version 1.0.0
 */
@Component
@Slf4j
public class RequestClient {
    @Autowired
    private ClientProxyProperties proxyProperties;

    private static final String DEFAULT_HOST = "127.0.0.1";

    public Channel getChannel(){
        return ProxyClientChannelContextHolder.getChannel();
    }

    public void sendProxyRequest(String host,Integer port){
        sendProxyRequest(host,port,null,null);
    }
    public void sendProxyRequest(String proxyHost,Integer proxyPort,String expectHost,Integer expectPort){
        Channel channel = getChannel();
        if(channel.isWritable() &&channel.isOpen()){
            ProxyRequest request = ProxyRequest.builder().expectHost(expectHost).expectPort(expectPort).proxyHost(proxyHost).proxyPort(proxyPort).build();
            Message<ProxyRequest> message = Message.getDefaultMessage(request, RequestType.CLIENT_CONNECT_REQUEST.getCode());
            channel.writeAndFlush(message);
        }
    }

    public void sendProxyRequest(Integer port){
        sendProxyRequest(DEFAULT_HOST,port);
    }
}
