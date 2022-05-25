package io.github.uginx.client.netty.handler.resp;

import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.github.uginx.core.model.CommonResp;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wengchengjian
 * @date 2022/5/25-13:31
 */
@Handler
public class ClientAuthRespHandler implements ServiceHandler<Message<CommonResp<String>>> {
    @Override
    public RequestType getSupportTypes() {
        return RequestType.CLIENT_AUTH_RESPONSE;
    }

    @Override
    public void doService(ChannelHandlerContext ctx, Message<CommonResp<String>> message) {
        CommonResp<String> res = message.getData();

        Integer code = res.getCode();

        String msg = res.getMsg();

        if(StatusCode.CLIENT_AUTH_SUCCESS.getCode().equals(code)){
            log.info("Client auth succuess");
        }else{
            log.error("Client auth failed. Reason:{}",msg);
        }
    }
}
