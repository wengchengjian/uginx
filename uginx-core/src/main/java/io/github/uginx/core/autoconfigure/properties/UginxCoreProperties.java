package io.github.uginx.core.autoconfigure.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 22:16
 * @Version 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "uginx.core")
public class UginxCoreProperties {

    public String compress = "gzip";

    public String codec = "kyro";

}
