package io.github.uginx.client.cmd;

import io.github.uginx.client.netty.ProxyClientChannelContextHolder;
import io.github.uginx.client.utils.RequestClient;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @Author 翁丞健
 * @Date 2022/5/25 22:00
 * @Version 1.0.0
 */
@ShellComponent
@Slf4j
public class ProxyCommand {

    @Autowired
    private RequestClient client;

    @ShellMethod("proxy server")
    public void proxy(String host,Integer port){
        client.sendProxyRequest(host,port);
    }
}
