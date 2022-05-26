package io.github.uginx.core.support;

import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.ProxyDispatcher;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wengchengjian
 * @date 2022/5/25-16:57
 */
@RequiredArgsConstructor
@Slf4j
public class HandlerDispatcher extends SimpleChannelInboundHandler<Message> implements ProxyDispatcher<Message> {
    private final List<ServiceHandler<Message>> serviceHandlerList;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        doDispatch(ctx,message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("{} has connected",ctx.channel().remoteAddress());
    }

    @Override
    public List<ServiceHandler<Message>> getHandlers() {
        return serviceHandlerList;
    }
}
