package io.github.uginx.sh.runner;

import io.github.uginx.core.NettyServerBootstrap;
import io.github.uginx.sh.config.ServerProxyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:40
 * @Version 1.0.0
 */
@Component
public class ProxyServerRunner implements ApplicationRunner {

    @Autowired
    private NettyServerBootstrap bootstrap;

    @Autowired
    private ServerProxyProperties serverProxyProperties;

    @Autowired
    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    simpleAsyncTaskExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            List<ServerProxyProperties.Server> servers = serverProxyProperties.getServers();

            for (ServerProxyProperties.Server server : servers) {
                //代理服务

            }
          }
        });
    }
}
