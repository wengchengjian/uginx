package io.github.uginx.core.protocol.handler;

import io.github.uginx.core.constants.MessageConstant;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wengchengjian
 * @date 2022/5/25-11:07
 */
@Slf4j
public class HeartBeatReqHandler implements ServiceHandler<Message> {
    @Override
    public RequestType getSupportTypes() {
        return RequestType.HEART_BEAT_REQUEST;
    }

    @Override
    public void doService(ChannelHandlerContext ctx, Message proxyMessage) {
        Message<String> pong = Message.getDefaultMessage(MessageConstant.PONG, RequestType.HEART_BEAT_RESPONSE.getCode());

        ctx.writeAndFlush(pong);
    }
}
