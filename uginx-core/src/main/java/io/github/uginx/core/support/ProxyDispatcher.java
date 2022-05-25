package io.github.uginx.core.support;

import io.github.uginx.core.exception.HandlerNotFountException;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;

import java.util.List;

/**
 * @author wengchengjian
 * @date 2022/5/25-10:42
 */
public interface ProxyDispatcher<T> {

    /**
     * 处理器分发
     * @param ctx
     * @param message
     */
    default void doDispatch(ChannelHandlerContext ctx, T message){
        if(message instanceof Message){
            Message msg = (Message) message;

            byte type = msg.getType();

            ServiceHandler<T> serviceHandler = getHandler(type);

            serviceHandler.handle(ctx,message);
        }

    }


    /**
     * 获取处理器
     * @param type
     * @return
     */
    default ServiceHandler<T> getHandler(byte type){
        @NonNull  List<ServiceHandler<T>> handlers = getHandlers();

        for (ServiceHandler<T> handler : handlers) {
            if(handler.support(type)){
                return handler;
            }
        }
        throw new HandlerNotFountException("there is not a handler for you type");
    }

    /**
     * 获取处理器集合
     * @return
     */
    List<ServiceHandler<T>> getHandlers();
}
