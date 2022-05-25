package io.github.uginx.core.protocol.handler;

import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wengchengjian
 * @date 2022/5/25-11:04
 */
@Slf4j
public class HeartBeatRespHandler implements ServiceHandler<Message> {

    @Override
    public RequestType getSupportTypes() {
        return RequestType.HEART_BEAT_RESPONSE;
    }

    @Override
    public void doService(ChannelHandlerContext ctx, Message proxyMessage) {
        log.info("received {} msg: {}",ctx.channel().remoteAddress(),proxyMessage.getData());
    }
}
