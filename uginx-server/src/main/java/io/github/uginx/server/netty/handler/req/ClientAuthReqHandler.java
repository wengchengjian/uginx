package io.github.uginx.server.netty.handler.req;
import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.github.uginx.server.netty.ProxyClientChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static io.github.uginx.core.constants.RequestType.CLIENT_AUTH_REQUEST;
import static io.github.uginx.core.constants.RequestType.CLIENT_AUTH_RESPONSE;
import static io.github.uginx.core.constants.StatusCode.CLIENT_AUTH_Failure;
import static io.github.uginx.core.constants.StatusCode.CLIENT_AUTH_SUCCESS;


/**
 * @Author 翁丞健
 * @Date 2022/4/29 23:13
 * @Version 1.0.0
 */
@Handler
@Slf4j
public class ClientAuthReqHandler implements ServiceHandler<Message> {
    @Override
    public RequestType getSupportTypes() {

        return CLIENT_AUTH_REQUEST;
    }

    @Override
    public RequestType getReturnType() {
        return CLIENT_AUTH_RESPONSE;
    }

    /**
     * 服务将clientKey 对应的 channel存储起来
     * @param ctx
     * @param proxyMessage
     */
    @Override
    public void doService(ChannelHandlerContext ctx, Message proxyMessage) {
        String clientKey = (String) proxyMessage.getData();
        String reason = null;
        try{
            if(StringUtils.isBlank(clientKey)){
                //TODO 优化提示信息
                log.error("clientKey:{} not found ,{}",clientKey,ctx.channel());
                reason = String.format("clientKey:{} not found",clientKey);
                failure(ctx,reason);
                return;
            }
            if(ProxyClientChannelManager.containsClientKey(clientKey)){
                Channel channel = ProxyClientChannelManager.getClientChannel(clientKey);
                //TODO 优化提示信息
                log.error("exist channel for key {}, {}", clientKey, channel);
                reason = String.format("exist channel for key %s, %s", clientKey, channel);

                failure(ctx,reason);
                return;
            }

            log.info("set clientKey => channel, {}, {}", clientKey, ctx.channel());
            ProxyClientChannelManager.addClientChannel(clientKey, ctx.channel());
            success(ctx,CLIENT_AUTH_SUCCESS);

        }catch (IllegalArgumentException e){
            log.info("{} client auth failed. clientKey:{}",ctx.channel().remoteAddress(),clientKey);
            failure(ctx,CLIENT_AUTH_Failure);
        }
    }


}
