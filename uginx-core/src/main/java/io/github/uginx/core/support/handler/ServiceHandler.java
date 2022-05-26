package io.github.uginx.core.support.handler;


import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.github.uginx.core.model.CommonResp;
import io.github.uginx.core.protocol.message.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.uginx.core.constants.StatusCode.Failure;
import static io.github.uginx.core.constants.StatusCode.SUCCESS;


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


    default void handle(ChannelHandlerContext ctx, T message){
            try{
                doService(ctx,  message);
            }catch (Exception e){
                log.error("处理异常:{}", e);
                exceptinCaguht(ctx, e);
            }
    }

    void doService(ChannelHandlerContext ctx, T message);

    default void exceptinCaguht(ChannelHandlerContext ctx, Throwable cause){
        log.error("关闭连接:{}", cause);
        ctx.close();
    }

    default RequestType getReturnType(){
        return null;
    }

    default void success(ChannelHandlerContext ctx, Object returnMessage){
        success(ctx,SUCCESS, returnMessage);
    }

    default void success(ChannelHandlerContext ctx, StatusCode statusCode,Object returnMessage){
        ctx.writeAndFlush(getResponse(statusCode,returnMessage));
    }

    default void success(ChannelHandlerContext ctx, StatusCode statusCode){
        success(ctx,statusCode,null);
    }
    default Message getResponse(StatusCode statusCode, String msg){
        return Message.getDefaultMessage(CommonResp.of(statusCode,msg),getReturnType().getCode());
    }

    default Message getResponse(StatusCode statusCode, Object ret){
        return Message.getDefaultMessage(CommonResp.of(statusCode,ret),getReturnType().getCode());
    }

    default void failure(ChannelHandlerContext ctx, String returnMessage){
        failure(ctx,Failure,returnMessage);
    }

    default void failure(ChannelHandlerContext ctx, StatusCode statusCode, Object data){
        ctx.writeAndFlush(getResponse(statusCode,data));
    }

    default void failure(ChannelHandlerContext ctx, StatusCode statusCode){
        failure(ctx, statusCode,null);
    }
    default void failure(ChannelHandlerContext ctx, StatusCode statusCode,String reason){
        ctx.writeAndFlush(Message.getDefaultMessage(CommonResp.Failure(statusCode,reason),getReturnType().getCode()));
    }

}
