package io.github.uginx.server.netty;

import io.github.uginx.core.model.Container;
import io.github.uginx.core.protocol.handler.IdeaCheckHandler;
import io.github.uginx.core.protocol.handler.ProxyMessageDecoder;
import io.github.uginx.core.protocol.handler.ProxyMessageEncoder;
import io.github.uginx.core.support.HandlerDispatcher;
import io.github.uginx.server.config.ServerProxyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 21:59
 * @Version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyServerBootstrap implements Container {

    @NonNull
    private final ServerProxyProperties properties;
    @NonNull
    private final NioEventLoopGroup bossGroup;
    @NonNull
    private final NioEventLoopGroup workGroup;
    @NonNull
    private final ServerBootstrap serverBootstrap;

    @NonNull
    private final ProxyMessageDecoder decoder;

    @NonNull
    private final ProxyMessageEncoder encoder;
    @NonNull
    private final IdeaCheckHandler ideaCheckHandler;

    @NonNull
    private final HandlerDispatcher dispatcher;

    private ChannelFuture future;

    @Override
    @SneakyThrows({InterruptedException.class})
    public void start(){
        int port = properties.getPort();

        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(decoder);
                        pipeline.addLast(encoder);
                        pipeline.addLast(ideaCheckHandler);
                        pipeline.addLast(dispatcher);
                    }
                });
        future = serverBootstrap.bind(port).sync();

        future.channel().closeFuture().sync();
        log.info("netty proxy server has started on {}. proxy is effective",port);
    }
    @Override
    public void stop(){
        log.info("waiting for the last data send finished.");
        future.channel().writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("Closing the channel.");
                future.channel().close();

                log.info("Closing the BossGroup ThreadGroup.");
                bossGroup.shutdownGracefully();

                log.info("Closing the WorkGroup ThreadGroup.");
                workGroup.shutdownGracefully();
            }
        });

    }



}
