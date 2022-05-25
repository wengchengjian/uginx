package io.github.uginx.core.protocol.handler;

import io.github.uginx.core.autoconfigure.annotation.Handler;
import io.github.uginx.core.constants.MessageConstant;
import io.github.uginx.core.constants.RequestType;
import io.github.uginx.core.exception.NotSupportProtocolException;
import io.github.uginx.core.exception.NotSupportedVersionException;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.support.factory.CompressFactory;
import io.github.uginx.core.support.factory.RequestFactory;
import io.github.uginx.core.support.factory.SerializerFactory;
import io.github.uginx.core.utils.compress.Compress;
import io.github.uginx.core.utils.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteOrder;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 21:40
 * @Version 1.0.0
 */
@Slf4j
public class ProxyMessageDecoder extends LengthFieldBasedFrameDecoder {

    @Autowired
    private SerializerFactory serializerFactory;

    @Autowired
    private CompressFactory compressFactory;

    private static final int MAX_FRAME_SIZE  = 2*1024*1024;

    private static final int LEGNTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    public ProxyMessageDecoder(){
        this(MAX_FRAME_SIZE,LEGNTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP);
    }

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public ProxyMessageDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Message decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 长度最小不能小于16
        if(in.readableBytes() < MessageConstant.HEAD_LENGTH){
            try{
                return decodeFrame(in);
            }catch (Exception e){
                log.error("decode error :{}",e.getMessage());
            }
        }
        return null;
    }

    /**
     * 检查自定义协议是否正常
     * @param in
     */
    public void checkProtocol(ByteBuf in){
        // 检查魔数是否正确
        checkMagicNumber(in);
        // 检查版本号是否正确
        checkVersion(in);
    }

    public Message getMessageHeader(ByteBuf in){
        // 获取编码类型
        byte codec = in.readByte();
        // 获取消息压缩格式
        byte compress = in.readByte();
        // 获取消息类型
        byte messageType = in.readByte();
        // 跳过填充的数据
        in.skipBytes(MessageConstant.FILL_NUMBER.length);

        return Message
                .builder()
                .compress(compress)
                .codec(codec)
                .type(messageType)
                .build();
    }

    public Object getMessageBody(int bodyLength,Message message,ByteBuf in){
        Object ret = null;

        if(bodyLength > 0){
            byte[] body = new byte[bodyLength];

            in.readBytes(body);

            Serializer serializer = serializerFactory.getInstance(message.getCodec());

            Compress compresser = compressFactory.getInstance(message.getCompress());

            body = compresser.decompress(body);

            ret = serializer.deserialize(body,RequestType.getRequestType(message.getType()));
        }

        return ret;
    }

    private Message decodeFrame(ByteBuf in) {
        checkProtocol(in);
        // 获取消息总长度
        int fullMessageLength = in.readInt();

        Message message = getMessageHeader(in);
        // TODO 待优化
//        if(message.getType() == RequestType.HEART_BEAT_REQUEST.getCode()){
//            message.setData(MessageConstant.PING);
//            return message;
//        }
//        if(message.getType() == RequestType.HEART_BEAT_RESPONSE.getCode()){
//            message.setData(MessageConstant.PONG);
//            return message;
//        }
        int bodyLength = fullMessageLength - MessageConstant.HEAD_LENGTH;

        Object messageBody = getMessageBody(bodyLength, message, in);

        if(messageBody!=null){
            message.setData(messageBody);
        }else{
            //TODO 替换自定义异常
            log.error("message: {} happend error. body is null",message);
            throw new RuntimeException("message body may be null or empty.please check this message");
        }

        return message;
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();

        if(version!= MessageConstant.VERSION){
            log.error("not supported version :{}",version);
            throw new NotSupportedVersionException("not supported version");
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        byte[] bytes = new byte[MessageConstant.MAGIC_NUMBER.length];

        in.readBytes(bytes);

        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] != MessageConstant.MAGIC_NUMBER[i]){
                throw new NotSupportProtocolException("magic number error");
            }
        }
    }


}
