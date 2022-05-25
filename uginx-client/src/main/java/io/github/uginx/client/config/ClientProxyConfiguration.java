package io.github.uginx.client.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wengchengjian
 * @date 2022/5/25-15:41
 */
@Configuration
public class ClientProxyConfiguration {
    @Bean("workGroup")
    public NioEventLoopGroup workGroup(){
        return new NioEventLoopGroup();
    }

    @Bean("bootStrap")
    public Bootstrap bootstrap(){
        return new Bootstrap();
    }
}
