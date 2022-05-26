package io.github.uginx.server.netty.handler.req;

import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.github.uginx.core.model.ProxyRequest;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.github.uginx.server.config.ServerProxyProperties;
import io.github.uginx.server.netty.ProxyClientChannelManager;
import io.github.uginx.server.netty.handler.DataTransHandler;
import io.github.uginx.server.support.ContainerHelper;
import io.github.uginx.server.utils.ArgValidationUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.github.uginx.core.constants.StatusCode.*;

/**
 * @author wengchengjian
 * @date 2022/5/25-13:52
 */
@Slf4j
@Handler
@RequiredArgsConstructor
public class ClientProxyReqHandler implements ServiceHandler<Message<ProxyRequest>> {

    private final ServerProxyProperties properties;

    private final ContainerHelper containerHelper;

    @Override
    public RequestType getSupportTypes() {
        return RequestType.CLIENT_CONNECT_REQUEST;
    }

    @Override
    public RequestType getReturnType() {
        return RequestType.CLIENT_CONNECT_RESPONSE;
    }


    @Override
    public void doService(ChannelHandlerContext ctx, Message<ProxyRequest> message) {

        log.info("received proxy request from {}.",ctx.channel().remoteAddress());

        ProxyRequest proxyRequest = message.getData();

        String expectHost = getServerHost(proxyRequest.getExpectHost());

        Integer expectPort = getServerPort(proxyRequest.getExpectPort());

        String proxyHost = proxyRequest.getProxyHost();

        Integer proxyPort = proxyRequest.getProxyPort();

        String clientKey = proxyRequest.getClientKey();

        log.info("proxy client:{} expect server bind on {}:{} and proxy server {}:{}",ctx.channel().remoteAddress(),expectHost,expectPort,proxyHost,proxyPort);
        // 创建代理server
        containerHelper.registerProxyServer(expectHost, expectPort,proxyHost,proxyPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture server) throws Exception {
                if(server.isSuccess()){
                    log.info("bind proxy server on {} success",server.channel().localAddress());
                    // 保存新开启的代理server的channel
                    ProxyClientChannelManager.addClientProxyChannel(clientKey,expectHost,expectPort,server.channel());
                    log.info("{}:{} now is proxy by {}:{}",proxyHost,proxyPort,expectHost,expectPort);
                    success(ctx, CLIENT_PROXY_SUCCESS,String.format("%s:%d now is proxyed by %s:%d",proxyHost,proxyPort,expectHost,expectPort));
                }else{
                    failure(ctx,Failure,String.format("{}:{} proxy failed",proxyHost,proxyPort));
                }
            }
        });
    }

    private Integer getServerPort(Integer expectPort) {
        return ArgValidationUtil.getRandomPort();
    }

    private String getServerHost(String expectHost) {
        if(expectHost!=null){
            return expectHost;
        }
        return "localhost";
    }

    private DefaultFullHttpResponse getResponse(HttpResponseStatus statusCode, String message) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, statusCode, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }
}
