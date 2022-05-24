package io.github.uginx.server.netty;

import io.github.uginx.server.netty.handler.UginxHttpProxyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 21:59
 * @Version 1.0.0
 */
@Slf4j
public class NettyServerBootstrap {

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();

    private NioEventLoopGroup workGroup = new NioEventLoopGroup();

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    private Integer bindPort = 8787;

    private ChannelFuture future;

    @SneakyThrows({InterruptedException.class})
    public void start(){
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //Http编解码器
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(100*1024*1024));
                        //Http代理服务
                        pipeline.addLast(new UginxHttpProxyHandler());
                    }
                });
        future = serverBootstrap.bind(bindPort).sync();

        future.channel().closeFuture().sync();
        log.info("netty proxy server has started on {}. proxy is effective",bindPort);
    }
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
