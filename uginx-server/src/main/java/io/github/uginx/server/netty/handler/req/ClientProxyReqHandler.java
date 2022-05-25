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

import static io.github.uginx.core.constants.StatusCode.Failure;
import static io.github.uginx.core.constants.StatusCode.SUCCESS;

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

    private static final Integer TIME_OUT = 5000;

    @Override
    public void doService(ChannelHandlerContext ctx, Message<ProxyRequest> message) {

        ProxyRequest proxyRequest = message.getData();

        String expectHost = getServerHost(proxyRequest.getExpectHost());

        Integer expectPort = getServerPort(proxyRequest.getExpectPort());

        String proxyHost = proxyRequest.getProxyHost();

        Integer proxyPort = proxyRequest.getProxyPort();

        String clientKey = proxyRequest.getClientKey();

        // 创建代理server
        containerHelper.registerProxyServer(expectHost, expectPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture server) throws Exception {
                if(server.isSuccess()){
                    // 保存新开启的代理server的channel
                    ProxyClientChannelManager.addClientProxyChannel(clientKey,expectHost,expectPort,server.channel());
                    // 连接到目标服务器
                    connectToTarget(server.channel(), proxyHost, proxyPort, TIME_OUT).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if(future.isSuccess()){
                                // 给代理server新增处理器
                                server.channel().pipeline().addLast(new DataTransHandler(future.channel()));
                                // 返回数据
                                success(ctx, SUCCESS,String.format("%s:%d now is proxyed by %s:%d",proxyHost,proxyPort,expectHost,expectPort));
                            }else{
                                failure(ctx,Failure,String.format("{}:{} proxy failed",proxyHost,proxyPort));
                            }
                        }
                    });
                }else{
                    failure(ctx,Failure,String.format("{}:{} proxy failed",proxyHost,proxyPort));
                }
            }
        });

        // 连接目标服务器




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
