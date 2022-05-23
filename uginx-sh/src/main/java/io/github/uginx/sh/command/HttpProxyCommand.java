package io.github.uginx.sh.command;

import io.github.uginx.core.NettyServerBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:41
 * @Version 1.0.0
 */
@ShellComponent
@Slf4j
public class HttpProxyCommand {

    @Autowired
    private NettyServerBootstrap bootstrap;

    @Autowired
    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

    @ShellMethod(value = "proxy server address to other address",key = "proxy")
    public void proxy(String host,Integer port){
        log.info("waiting for proxy {}:{}",host,port);
        simpleAsyncTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bootstrap.start(host,port);
            }
        });
    }
    @ShellMethod("stop the proxy server")
    public void stop(){
        log.info("waiting for stop proxy server {}",bootstrap);
        simpleAsyncTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bootstrap.stop();
            }
        });
    }
}
