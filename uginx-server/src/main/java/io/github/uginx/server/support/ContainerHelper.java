package io.github.uginx.server.support;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.proxy.HttpProxyHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wengchengjian
 * @date 2022/5/25-14:38
 */
@Slf4j
@RequiredArgsConstructor
public class ContainerHelper {

    private final NioEventLoopGroup bossGroup;

    private final NioEventLoopGroup workGroup;


    public ChannelFuture registerProxyServer(String host,Integer port){
        ServerBootstrap proxyServer = createProxyServer();

        InetSocketAddress address = new InetSocketAddress(host, port);

        initPipline(proxyServer);

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
     *  设置处理器
     * @param bootstrap
     */
    public  void initPipline(ServerBootstrap bootstrap){
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 什么都不做
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
