package io.github.uginx.core.constants;


import io.github.uginx.core.model.CommonResp;
import io.github.uginx.core.model.ProxyRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 翁丞健
 * @Date 2022/4/30 18:31
 * @Version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum RequestType {
    /**
     * 客户端请求认证请求包
     */
    CLIENT_AUTH_REQUEST((byte) 1,String.class),

    /**
     * 客户端请求认证响应包
     */
    CLIENT_AUTH_RESPONSE((byte) 2, CommonResp.class),

    /**
     * 客户端请求代理请求包
     */
    CLIENT_CONNECT_REQUEST((byte) 3, ProxyRequest.class),

    /**
     * 客户端请求代理响应包
     */
    CLIENT_CONNECT_RESPONSE((byte) 4,CommonResp.class),

    /**
     * 客户端请求关闭代理请求包
     */
    CLIENT_DISCONNECT_REQUEST((byte) 5,ProxyRequest.class),

    /**
     * 客户端请求关闭代理响应包
     */
    CLIENT_DISCONNECT_RESPONSE((byte) 6,CommonResp.class),

    /**
     * 心跳请求包
     */
    HEART_BEAT_REQUEST((byte) 9,String.class),

    /**
     * 心跳响应包
     */
    HEART_BEAT_RESPONSE((byte) 10,CommonResp.class);

    private final byte code;

    private Class<?> clazz;

    public static Class<?> getRequestType(byte code) {
        for (RequestType requestType : RequestType.values()) {
            if (requestType.getCode() == code) {
                return requestType.getClazz();
            }
        }
        throw new NoClassDefFoundError(String.format("there is not a class for code: {}",code));
    }

}
