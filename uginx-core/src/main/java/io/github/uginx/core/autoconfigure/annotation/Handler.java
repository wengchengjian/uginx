package io.github.uginx.core.autoconfigure.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wengchengjian
 * @date 2022/5/25-10:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Handler {
}
