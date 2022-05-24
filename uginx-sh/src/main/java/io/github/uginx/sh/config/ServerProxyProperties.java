package io.github.uginx.sh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.UUID;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:41
 * @Version 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "uginx")
public class ServerProxyProperties {

    /**
     * 需要代理的服务
     */
    private List<Server> servers;


    @Data
    public static class Server{
        /**
         * 默认代理本地的服务
         */
        private String host = "localhost";

        private String port;

        /**
         * 唯一标识代理服务的key，默认为UUID
         */
        private String clientKey = UUID.randomUUID().toString();

    }
}
