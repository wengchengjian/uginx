package io.github.uginx.client;

import io.github.uginx.client.config.ClientProxyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Author 翁丞健
 * @Date 2022/5/24 21:51
 * @Version 1.0.0
 */
@SpringBootApplication
@EnableConfigurationProperties(ClientProxyProperties.class)
public class ClientProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientProxyApplication.class,args);
    }
}
