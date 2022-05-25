package io.github.uginx.client.runner;

import io.github.uginx.client.netty.NettyClientBootstrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/5/25 21:36
 * @Version 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ClientStartRunner implements ApplicationRunner {

    private final NettyClientBootstrap bootstrap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bootstrap.start();
    }
}
