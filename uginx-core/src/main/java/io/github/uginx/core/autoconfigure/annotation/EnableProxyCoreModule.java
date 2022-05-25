package io.github.uginx.core.autoconfigure.annotation;

import io.github.uginx.core.autoconfigure.config.UginxCoreAutoConfiguration;
import io.github.uginx.core.autoconfigure.properties.UginxCoreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


import java.lang.annotation.*;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 22:14
 * @Version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableConfigurationProperties(UginxCoreProperties.class)
@Import(UginxCoreAutoConfiguration.class)
@ComponentScan(basePackages = "io.github.uginx.core.support")
public @interface EnableProxyCoreModule {

}
