package io.github.uginx.server.runner;

import io.github.uginx.server.netty.NettyServerBootstrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/5/25 21:38
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ServerStartRunner implements ApplicationRunner {
    private final NettyServerBootstrap bootstrap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bootstrap.start();
    }
}
