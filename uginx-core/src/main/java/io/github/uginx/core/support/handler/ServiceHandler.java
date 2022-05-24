package io.github.uginx.core.support.handler;


import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @Author 翁丞健
 * @Date 2022/4/29 23:07
 * @Version 1.0.0
 */
public interface ServiceHandler<T> {
    Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    RequestType getSupportTypes();


    default boolean support(byte type){
        return getSupportTypes().getCode() == type;
    }

    default boolean support(RequestType type){
        return getSupportTypes().equals(type);
    }


    default void handle(ChannelHandlerContext ctx, T proxyMessage){
            try{
                doService(ctx,  proxyMessage);
            }catch (Exception e){
                log.error("处理异常:{}", e);
                exceptinCaguht(ctx, e);
            }
    }

    void doService(ChannelHandlerContext ctx, T proxyMessage);

    default void exceptinCaguht(ChannelHandlerContext ctx, Throwable cause){
        log.error("关闭连接:{}", cause);
        ctx.close();
    }
}
