package io.github.uginx.server.netty.handler.req;

import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.constants.StatusCode;
import io.github.uginx.core.model.ProxyRequest;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.handler.ServiceHandler;
import io.github.uginx.server.netty.ProxyClientChannelManager;
import io.github.uginx.server.netty.handler.DataTransHandler;
import io.github.uginx.server.support.ContainerHelper;
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

import static io.github.uginx.core.constants.StatusCode.SUCCESS;

/**
 * @author wengchengjian
 * @date 2022/5/25-13:52
 */
@Slf4j
@Handler
@RequiredArgsConstructor
public class ClientProxyReqHandler implements ServiceHandler<Message<ProxyRequest>> {

    private final ContainerHelper containerHelper;

    @Override
    public RequestType getSupportTypes() {
        return RequestType.CLIENT_CONNECT_REQUEST;
    }

    @Override
    public RequestType getReturnType() {
        return RequestType.CLIENT_CONNECT_RESPONSE;
    }

    private static final Integer TIME_OUT = 5000;

    @Override
    public void doService(ChannelHandlerContext ctx, Message<ProxyRequest> message) {

        ProxyRequest proxyRequest = message.getData();

        String expectHost = proxyRequest.getExpectHost();

        Integer expectPort = proxyRequest.getExpectPort();

        String proxyHost = proxyRequest.getProxyHost();

        Integer proxyPort = proxyRequest.getProxyPort();

        String clientKey = proxyRequest.getClientKey();

        // 创建代理server
        ChannelFuture server = containerHelper.registerProxyServer(expectHost, expectPort);
        // 保存新开启的代理server的channel
        ProxyClientChannelManager.addClientProxyChannel(clientKey,expectHost,expectPort,server.channel());
        // 连接目标服务器
        ChannelFuture target = connectToTarget(server.channel(), proxyHost, proxyPort, TIME_OUT);
        // 给代理server新增处理器
        server.channel().pipeline().addLast(new DataTransHandler(target.channel()));

        success(ctx, SUCCESS);

    }

    private ChannelFuture connectToTarget(final Channel channel, String host, int port, int  timeout){
        return getChannelFuture(channel, host, port, timeout);
    }

    public static ChannelFuture getChannelFuture(Channel channel, String host, int port, int timeout) {
        return new Bootstrap().group(channel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DataTransHandler(channel));
                    }
                }).connect(host,port);
    }

    private DefaultFullHttpResponse getResponse(HttpResponseStatus statusCode, String message) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, statusCode, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }
}
