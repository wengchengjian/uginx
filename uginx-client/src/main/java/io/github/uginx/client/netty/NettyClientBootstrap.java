package io.github.uginx.client.netty;

import io.github.uginx.client.config.ClientProxyProperties;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.model.Container;
import io.github.uginx.core.protocol.handler.ProxyMessageDecoder;
import io.github.uginx.core.protocol.handler.ProxyMessageEncoder;
import io.github.uginx.core.protocol.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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

    private final Bootstrap bootstrap;

    private final NioEventLoopGroup workGroup;

    private final ClientProxyProperties properties;

    private final ProxyMessageEncoder messageEncoder;

    private final ProxyMessageDecoder messageDecoder;

    private final HandlerDispatcher handlerDispatcher;

    private Channel channel;

    @Override
    @SneakyThrows(InterruptedException.class)
    public synchronized void start() {
        if(!ProxyClientChannelContextHolder.getChannel().isOpen()){
            bootstrap.group(workGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(messageDecoder);
                    pipeline.addLast(messageEncoder);
                    pipeline.addLast(handlerDispatcher);
                }
            });

            bootstrap.connect(properties.getServerHost(),properties.getServerPort()).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        // 保存客户端-服务端连接
                        ProxyClientChannelContextHolder.setChannel(future.channel());
                        // 建立连接以后发送认证请求
                        sendAuthData(channel,properties.getClientKey());
                    }
                }

                private void sendAuthData(Channel channel, String clientKey) {
                    Message<String> message = Message.getDefaultMessage(clientKey, RequestType.CLIENT_AUTH_REQUEST.getCode());

                    channel.writeAndFlush(message);
                }
            });
        }
    }

    @Override
    public synchronized void stop() {

    }
}
