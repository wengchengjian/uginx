package io.github.uginx.client.netty.handler.resp;

import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.github.uginx.core.model.CommonResp;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wengchengjian
 * @date 2022/5/25-15:38
 */
@Handler
public class ClientProxyRespHandler implements ServiceHandler<Message<CommonResp<String>>> {
    @Override
    public RequestType getSupportTypes() {
        return RequestType.CLIENT_CONNECT_RESPONSE;
    }

    @Override
    public void doService(ChannelHandlerContext ctx, Message<CommonResp<String>> message) {
        CommonResp<String> res = message.getData();

        Integer code = res.getCode();

        String msg = res.getMsg();

        if(StatusCode.CLIENT_AUTH_SUCCESS.getCode().equals(code)){
            log.info("Client proxy succuess");
        }else{
            log.error("Client proxy failed. Reason:{}",msg);
        }
    }
}
