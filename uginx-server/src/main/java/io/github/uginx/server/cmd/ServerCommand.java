package io.github.uginx.server.cmd;

import io.github.uginx.server.netty.NettyServerBootstrap;
import io.github.uginx.server.netty.ProxyClientChannelManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 翁丞健
 * @Date 2022/5/25 21:48
 * @Version 1.0.0
 */
@RequiredArgsConstructor
@ShellCommandGroup("server")
@ShellComponent
@Slf4j
public class ServerCommand {

    private final NettyServerBootstrap bootstrap;

    private final ProxyClientChannelManager clientChannelManager;

    @ShellMethod("list the proxys ")
    public String list(){
        return "Hello";
    }

    @ShellMethod("start server")
    public void start(){
        bootstrap.start();
    }

    @ShellMethod("stop server")
    public void stop(){
        bootstrap.stop();
    }

    @ShellMethod("ping ")
    public void ping(String host,String port){

    }
}
