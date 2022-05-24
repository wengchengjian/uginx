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

    private static <T> CommonResp<T> Success(StatusCode success, T data) {
        return Success(success.getCode(),success.getMsg(),data);
    }

    private static <T> CommonResp<T> Success(Integer code, String msg, T data) {
        return new CommonResp<>(code,msg,data);
    }


    public static <T> CommonResp<T> Failure(T data){
        return Failure(StatusCode.Failure,data);
    }

    public static <T> CommonResp<T> Failure(){
        return Failure(StatusCode.Failure,null);
    }

    private static <T> CommonResp<T> Failure(StatusCode failure, T data) {
        return Failure(failure.getCode(),failure.getMsg(),data);
    }

    private static <T> CommonResp<T> Failure(Integer code, String msg, T data) {
        return new CommonResp<>(code,msg,data);
    }
}
