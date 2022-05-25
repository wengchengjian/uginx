package io.github.uginx.core.model;

import io.github.uginx.core.constants.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 翁丞健
 * @Date 2022/5/24 22:33
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResp<T> {
    private Integer code;

    private String msg;

    private T data;



    public static <T> CommonResp<T> Success(T data){
        return Success(StatusCode.SUCCESS,data);
    }

    public static <T> CommonResp<T> Success(){
        return Success(StatusCode.SUCCESS,null);
    }

    public static <T> CommonResp<T> Success(StatusCode success, T data) {
        return Success(success.getCode(),success.getMsg(),data);
    }

    public static <T> CommonResp<T> Success(Integer code, String msg, T data) {
        return new CommonResp<>(code,msg,data);
    }


    public static <T> CommonResp<T> Failure(T data){
        return Failure(StatusCode.Failure,data);
    }

    public static <T> CommonResp<T> Failure(){
        return Failure(StatusCode.Failure,null);
    }

    public static <T> CommonResp<T> Failure(String msg){
        return Failure(StatusCode.Failure.getCode(),msg,null);
    }

    public static <T> CommonResp<T> Failure(StatusCode failure, T data) {
        return Failure(failure.getCode(),failure.getMsg(),data);
    }

    public static <T> CommonResp<T> Failure(Integer code, String msg, T data) {
        return new CommonResp<>(code,msg,data);
    }

    public static CommonResp of(StatusCode statusCode, Object ret) {
        return new CommonResp(statusCode.getCode(),statusCode.getMsg(),ret);
    }

    public static CommonResp of(StatusCode statusCode, String msg) {
        return new CommonResp(statusCode.getCode(),msg,null);
    }
}
