package io.github.uginx.core.protocol.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static io.github.uginx.core.constants.CodecEnum.KYRO;
import static io.github.uginx.core.constants.CompressEnum.GZIP;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 21:37
 * @Version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> implements Serializable,Cloneable {

    /**
     * 消息类型
     */
    private byte type;

    /**
     * 编码格式
     */
    private byte codec;

    /**
     * 消息压缩格式
     */
    private byte compress;

    /**
     * 消息请求体
     */
    private T data;

    public static  <U> Message<U> getDefaultMessage(U data, byte type){
        return Message.<U>builder()
                .type(type)
                .codec(KYRO.getCode())
                .compress(GZIP.getCode())
                .data(data)
                .build();

    }


}
