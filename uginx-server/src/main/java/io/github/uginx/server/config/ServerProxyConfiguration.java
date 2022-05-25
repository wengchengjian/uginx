package io.github.uginx.server.config;

import io.github.uginx.server.netty.NettyServerBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wengchengjian
 */
@Configuration
public class ServerProxyConfiguration {
    @Bean("bossGroup")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }
    @Bean("workGroup")
    public NioEventLoopGroup workGroup(){
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap serverBootstrap(){
        return new ServerBootstrap();
    }
}
