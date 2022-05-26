package io.github.uginx.client.netty;

import io.github.uginx.client.config.ClientProxyProperties;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.model.Container;
import io.github.uginx.core.protocol.handler.ProxyMessageDecoder;
import io.github.uginx.core.protocol.handler.ProxyMessageEncoder;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.HandlerDispatcher;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/5/24 21:52
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NettyClientBootstrap implements Container {

    @NonNull
    private final Bootstrap bootstrap;
    @NonNull
    private final NioEventLoopGroup workGroup;
    @NonNull
    private final ClientProxyProperties properties;
    @NonNull
    private final ProxyMessageEncoder messageEncoder;
    @NonNull
    private final ProxyMessageDecoder messageDecoder;
    @NonNull
    private final HandlerDispatcher handlerDispatcher;

    @Override
    @SneakyThrows(InterruptedException.class)
    public synchronized void start() {
        if(ProxyClientChannelContextHolder.getChannel()==null){
            bootstrap.group(workGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(messageDecoder);
                    pipeline.addLast(messageEncoder);
                    pipeline.addLast(handlerDispatcher);
                }
            });

            ChannelFuture future = bootstrap.connect(properties.getServerHost(), properties.getServerPort()).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("proxy client has started on:{}",future.channel().localAddress());
                        // 保存客户端-服务端连接
                        ProxyClientChannelContextHolder.setChannel(future.channel());
                        // 建立连接以后发送认证请求
                        sendAuthData(ProxyClientChannelContextHolder.getChannel(), properties.getClientKey());
                    }
                }

                private void sendAuthData(Channel channel, String clientKey) {
                    log.info("ready to send auth data to server");

                    Message<String> message = Message.getDefaultMessage(clientKey, RequestType.CLIENT_AUTH_REQUEST.getCode());

                    channel.writeAndFlush(message);
                }
            });

            future.channel().closeFuture();

        }
    }

    @Override
    public synchronized void stop() {
        Channel channel = ProxyClientChannelContextHolder.getChannel();
        if(channel!=null){
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            workGroup.shutdownGracefully();
            return;
        }
        log.error("you are not started client");
    }
}
