package io.github.uginx.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * @author wengchengjian
 * @date 2022/5/25-13:55
 */
@Data
@ConfigurationProperties(prefix = "uginx.client")
public class ClientProxyProperties {

    /**
     * 服务端的host
     */
    private String serverHost="localhost";

    /**
     * 服务端的port
     */
    private Integer serverPort = 7896;

    /**
     * 客户端唯一标识key，默认为UUID
     */
    private String clientKey = UUID.randomUUID().toString();

}
