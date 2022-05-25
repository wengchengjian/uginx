package io.github.uginx.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wengchengjian
 */
@Data
@ConfigurationProperties(prefix = "uginx.server")
public class ServerProxyProperties {

    /**
     * 默认Server端运行端口 7896
     */
    private Integer port = 7896;

    /**
     * 默认单节点server承受100个客户端连接
     */
    private Integer maxClientSize = 100;

    /**
     * 默认单个客户端可以代理的数量 10
     */
    private Integer singleClientProxySize = 10;

    /**
     * 默认最大代理服务数量
     */
    private Integer maxProxySize = 1000;
}
