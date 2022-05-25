package io.github.uginx.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 翁丞健
 * @Date 2022/5/24 22:31
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyRequest {
    /**
     * 需要代理的地址
     */
    private String proxyHost;

    /**
     * 需要代理的端口
     */
    private Integer proxyPort;

    /**
     * 期待代理后的端口
     */
    private Integer expectPort;

    /**
     * 期待代理后的地址
     */
    private String expectHost;

    /**
     * 客户端Key
     */
    private String clientKey;
}
