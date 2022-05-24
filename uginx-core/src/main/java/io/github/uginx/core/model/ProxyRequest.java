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
    private String host;

    private Integer port;
}
