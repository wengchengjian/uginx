package io.github.uginx.server.netty.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author 翁丞健
 * @Date 2022/5/23 22:04
 * @Version 1.0.0
 */
public class UginxHttpProxyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest fullHttpRequest) throws Exception {

        if(StringUtils.isBlank(host) || port == null){
            ctx.writeAndFlush(getResponse(HttpResponseStatus.BAD_REQUEST,"目标地址或者端口为空")).addListener(ChannelFutureListener.CLOSE);
            return;
        }


        fullHttpRequest.headers().set(HttpHeaderNames.HOST,host);

        ReferenceCountUtil.retain(fullHttpRequest);

        connectToRemote(ctx,host,port,1000).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    fullHttpRequest.headers().set(HttpHeaderNames.CONNECTION,"close");

                    future.channel().writeAndFlush(fullHttpRequest).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                //移除客户端的http编译码器
                                future.channel().pipeline().remove(HttpClientCodec.class);
                                //移除代理服务和请求端 通道之间的http编译码器和集合器
                                ctx.channel().pipeline().remove(HttpServerCodec.class);
                                ctx.channel().pipeline().remove(HttpObjectAggregator.class);
                                //移除后，让通道直接直接变成单纯的ByteBuf传输
                            }
                        }
                    });
                }else{
                    ReferenceCountUtil.retain(fullHttpRequest);
                    ctx.writeAndFlush(getResponse(HttpResponseStatus.BAD_REQUEST, "代理服务连接远程服务失败"))
                            .addListener(ChannelFutureListener.CLOSE);
                }
            }
        });
    }
    private ChannelFuture connectToRemote(final ChannelHandlerContext ctx, String host, int port, int  timeout){
        return new Bootstrap().group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpClientCodec());
                pipeline.addLast(new DataTransHandler(ctx.channel()));
            }
        }).connect(host,port);
    }

    private DefaultFullHttpResponse getResponse(HttpResponseStatus statusCode, String message) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, statusCode, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }
}
