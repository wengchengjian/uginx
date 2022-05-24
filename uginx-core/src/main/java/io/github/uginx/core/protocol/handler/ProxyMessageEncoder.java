package io.github.uginx.core.protocol.handler;


import io.github.uginx.core.constants.MessageConstant;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.factory.CompressFactory;
import io.github.uginx.core.support.factory.SerializerFactory;
import io.github.uginx.core.utils.compress.Compress;
import io.github.uginx.core.utils.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.github.uginx.core.constants.RequestType.*;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 21:40
 * @Version 1.0.0
 */
@Slf4j
public class ProxyMessageEncoder extends MessageToByteEncoder<Message> {

    @Autowired
    private CompressFactory compressFactory;

    @Autowired
    private SerializerFactory serializerFactory;

    /**
     * 写入16个字节的消息头
     * @param out
     * @param msg
     * @return 返回消息头的长度
     */
    protected int writeMessageHeader(ByteBuf out,Message msg){
        // 写入消息头魔数标识协议类型, 占5个字节
        out.writeBytes(MessageConstant.MAGIC_NUMBER);
        // 写入消息头版本号,占一个字节
        out.writeByte(MessageConstant.VERSION);
        // 写入长度域,占四个字节
        out.writerIndex(out.writerIndex()+4);

        byte codec = msg.getCodec();
        // 写入序列化类型,占一个字节
        out.writeByte(codec);
        byte compressType = msg.getCompress();
        // 写入压缩类型,占1个字节
        out.writeByte(compressType);
        // 写入消息类型,占1个字节
        out.writeByte(msg.getType());
        // 写入填充数据 占3个字节
        out.writeBytes(MessageConstant.FILL_NUMBER);

        return MessageConstant.HEAD_LENGTH;
    }

    /**
     * 写入消息体
     * @param msg
     * @param out
     * @return 消息体长度
     */
    public int writeMessageBody(ByteBuf out,Message msg){
        byte codec = msg.getCodec();

        byte compressType = msg.getCompress();

        // 只要不是心跳包，就需要写入消息体
        byte[] body = new byte[0];
        if(msg.getType()!=HEART_BEAT_REQUEST.getCode() && msg.getType()!=HEART_BEAT_RESPONSE.getCode()){

            Object data = msg.getData();
            // 获取指定的序列化工具
            Serializer serializer = serializerFactory.getInstance(codec);
            // 序列化消息体
            body = serializer.serialize(data);
            // 获取指定的压缩工具
            Compress compressUtil = compressFactory.getInstance(compressType);
            // 压缩消息体
            body = compressUtil.compress(body);
            // 最终写入消息体长度
        }

        if(!ArrayUtils.isEmpty(body)){
            out.writeBytes(body);
        }

        return body.length;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int fullLength = 0;
        // 写入消息头，消息头长度必须为
        fullLength += writeMessageHeader(out,msg);
        // 写入消息体
        fullLength += writeMessageBody(out,msg);
        // 记录最终消息长度
        int writeIndex = out.writerIndex();
        // 跳过魔数和版本号
        out.writerIndex(MessageConstant.MAGIC_NUMBER.length + 1);
        // 写入整个请求消息长度
        out.writeInt(fullLength);
        // 还原消息写入索引
        out.writerIndex(writeIndex);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("message encode failed:{}", cause.getMessage());
    }
}
