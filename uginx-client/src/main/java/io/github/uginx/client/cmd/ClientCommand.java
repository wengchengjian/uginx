package io.github.uginx.client.cmd;

import io.github.uginx.client.config.ClientProxyProperties;
import io.github.uginx.client.netty.NettyClientBootstrap;
import io.github.uginx.client.utils.RequestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @author wengchengjian
 * @date 2022/5/26-13:31
 */
@RequiredArgsConstructor
@ShellCommandGroup("client")
@ShellComponent
@Slf4j
public class ClientCommand {

    private final NettyClientBootstrap bootstrap;

    private final ClientProxyProperties properties;

    private final RequestClient client;


    @ShellMethod("start proxy client")
    public void start(){
        bootstrap.start();
    }

    @ShellMethod("proxy target server")
    public void proxy(String host,Integer port){
        client.sendProxyRequest(host,port);
    }

    @ShellMethod("show your Client Id")
    public String who(){
        return properties.getClientKey();
    }

}
