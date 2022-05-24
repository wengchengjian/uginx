package io.github.uginx.sh.config;

import io.github.uginx.server.NettyServerBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:45
 * @Version 1.0.0
 */
@Configuration
public class ShellConfiguration {
    @Bean
    public NettyServerBootstrap nettyServerBootstrap(){
        return new NettyServerBootstrap();
    }

    @Bean
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor(){
        return new SimpleAsyncTaskExecutor("netty-thread-");
    }
}
