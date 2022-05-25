package io.github.uginx.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/4/28 23:12
 * @Version 1.0.0
 */
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {
    private  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public  final ApplicationContext getContext(){
        if(applicationContext==null){
            log.warn("获取applicationContext失败,请检查当前是否已经初始化或者调用时机不对");
        }
        return applicationContext;
    }
}
