package io.github.uginx.core.autoconfigure.config;


import io.github.uginx.core.protocol.handler.*;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.HandlerDispatcher;
import io.github.uginx.core.support.factory.CompressFactory;
import io.github.uginx.core.support.factory.SerializerFactory;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.github.uginx.core.utils.ApplicationContextUtil;
import io.github.uginx.core.utils.compress.Compress;
import io.github.uginx.core.utils.compress.impl.GzipCompress;
import io.github.uginx.core.utils.serialize.Serializer;
import io.github.uginx.core.utils.serialize.impl.KryoSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 22:15
 * @Version 1.0.0
 */

@Configuration
public class UginxCoreAutoConfiguration {

    /**
     * 默认注入gzip压缩器
     */
    @Bean
    @ConditionalOnMissingBean
    public Compress compress() {
        return new GzipCompress();
    }

    /**
     * 注入压缩器工厂
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public CompressFactory compressFactory(){
        return new CompressFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    /**
     * 注入序列化器工厂
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SerializerFactory serializerFactory(){
        return new SerializerFactory();
    }

    /**
     * 默认注入kryo序列化器
     */
    @Bean
    @ConditionalOnMissingBean
    public Serializer serializer() {
        return new KryoSerializer();
    }

    /**
     * 默认注入解码器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ProxyMessageDecoder proxyMessageDecoder(){
        return new ProxyMessageDecoder();
    }

    /**
     * 默认注入编码器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ProxyMessageEncoder proxyMessageEncoder(){
        return new ProxyMessageEncoder();
    }

    /**
     * 心跳请求处理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HeartBeatRespHandler heartBeatRespHandler(){
        return new HeartBeatRespHandler();
    }

    /**
     * 心跳响应处理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HeartBeatReqHandler heartBeatReqHandler(){
        return new HeartBeatReqHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IdeaCheckHandler ideaCheckHandler(){
        return new IdeaCheckHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerDispatcher dispatcher(List<ServiceHandler<Message>> serviceHandlers){
        return new HandlerDispatcher(serviceHandlers);
    }



}
