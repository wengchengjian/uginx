package io.github.uginx.server.netty.handler.req;

import io.github.uginx.server.netty.handler.DataTransHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.github.uginx.core.constants.StatusCode.CLIENT_PROXY_SUCCESS;
import static io.github.uginx.core.constants.StatusCode.Failure;

/**
 * @author wengchengjian
 * @date 2022/5/26-15:03
 */
@Slf4j
public class ProxyRequestHandler extends SimpleChannelInboundHandler {

    private String proxyHost;

    private Integer proxyPort;

    private static final Integer TIME_OUT = 5000;

    public ProxyRequestHandler(InetSocketAddress proxyAdress) {
        this.proxyHost = proxyAdress.getHostName();
        this.proxyPort = proxyAdress.getPort();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {


        log.info("ready to proxy target server to {}:{}",proxyHost,proxyPort);
        ReferenceCountUtil.retain(msg);
        // 连接到目标服务器
        connectToTarget(ctx.channel(), proxyHost, proxyPort, TIME_OUT).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
//                    log.info("connect target server success");
                    future.channel().writeAndFlush(msg);
                }
            }
        });
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
}
