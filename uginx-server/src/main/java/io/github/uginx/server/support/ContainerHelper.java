package io.github.uginx.server.support;


import io.github.uginx.server.netty.handler.req.ProxyRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author wengchengjian
 * @date 2022/5/25-14:38
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ContainerHelper {

    private final NioEventLoopGroup bossGroup;

    private final NioEventLoopGroup workGroup;


    public ChannelFuture registerProxyServer(String host,Integer port, String proxyHost, Integer proxyPort){
        ServerBootstrap proxyServer = createProxyServer();

        InetSocketAddress address = new InetSocketAddress(host, port);

        InetSocketAddress proxyAdress = new InetSocketAddress(proxyHost,proxyPort);

        initPipline(proxyServer,proxyAdress);

        return proxyServer.bind(address);
    }

    /**
     * 初始化一个代理服务
     * @return server
     */
    public  ServerBootstrap createProxyServer(){
        return new ServerBootstrap()
                .group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024);
    }

    /**
     * 设置处理器
     *
     * @param bootstrap
     * @param proxyAdress
     */
    public  void initPipline(ServerBootstrap bootstrap, InetSocketAddress proxyAdress){
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 什么都不做
                ch.pipeline().addLast(new ProxyRequestHandler(proxyAdress));
            }
        });
    }




    public  void initProxyPipline(ChannelPipeline pipeline){

    }

    public void start(){

    }

    public void stop(){

    }
}
