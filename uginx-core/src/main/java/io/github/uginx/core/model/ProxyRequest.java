package io.github.uginx.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ProxyRequest {
    /**
     * 需要代理的地址，默认为本地
     */
    private String proxyHost = "localhost";

    /**
     * 需要代理的端口
     */
    private Integer proxyPort;

    /**
     * 期待代理后的端口
     */
    private Integer expectPort;

    /**
     * 期待代理后的地址，默认为本地
     */
    private String expectHost = "localhost";

    /**
     * 客户端Key
     */
    private String clientKey;
}
