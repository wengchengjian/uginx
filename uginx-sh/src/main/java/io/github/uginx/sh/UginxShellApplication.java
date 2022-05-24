package io.github.uginx.sh;

import io.github.uginx.sh.config.ServerProxyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:40
 * @Version 1.0.0
 */
@SpringBootApplication
@EnableConfigurationProperties({ServerProxyProperties.class})
public class UginxShellApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(UginxShellApplication.class, args);

    }
}
