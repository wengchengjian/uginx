package io.github.uginx.core.protocol.handler;

import io.github.uginx.core.constants.MessageConstant;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.protocol.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * TODO 优化为自定义自定义配置
 * @Author 翁丞健
 * @Date 2022/4/27 22:53
 * @Version 1.0.0
 */
@Slf4j
public class IdeaCheckHandler extends IdleStateHandler {
    private static final int READER_IDLE_TIME_SECONDS = 30;

    private static final int WRITER_IDLE_TIME_SECONDS = 30;

    private static final int ALL_IDLE_TIME_SECONDS = 60;

    public IdeaCheckHandler(){
        super(READER_IDLE_TIME_SECONDS,WRITER_IDLE_TIME_SECONDS,ALL_IDLE_TIME_SECONDS);
    }
    public IdeaCheckHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    public IdeaCheckHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    public IdeaCheckHandler(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(observeOutput, readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        if (IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT == evt) {
            log.debug("channel write timeout {}", ctx.channel());
            Message<String> msg = Message.getDefaultMessage(MessageConstant.PING,RequestType.HEART_BEAT_REQUEST.getCode());
            ctx.channel().writeAndFlush(msg);
        } else if (IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT == evt) {
            log.warn("channel read timeout {}", ctx.channel());
            ctx.channel().close();
        }
        super.channelIdle(ctx, evt);
    }
}
