package io.github.uginx.core.support;

import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.ProxyDispatcher;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wengchengjian
 * @date 2022/5/25-16:57
 */
@RequiredArgsConstructor
public class HandlerDispatcher extends SimpleChannelInboundHandler<Message> implements ProxyDispatcher<Message> {
    private final List<ServiceHandler<Message>> serviceHandlerList;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        doDispatch(ctx,message);
    }
    @Override
    public List<ServiceHandler<Message>> getHandlers() {
        return serviceHandlerList;
    }
}
