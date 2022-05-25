package io.github.uginx.server;

import io.github.uginx.core.autoconfigure.annotation.EnableProxyCoreModule;
import io.github.uginx.server.config.ServerProxyProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author 翁丞健
 * @Date 2022/5/24 21:47
 * @Version 1.0.0
 */
@EnableProxyCoreModule
@EnableConfigurationProperties(ServerProxyProperties.class)
@SpringBootApplication
public class ServerProxyApplication  {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServerProxyApplication.class, args);

    }


}
