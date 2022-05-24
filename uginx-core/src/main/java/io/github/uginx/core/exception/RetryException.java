package io.github.uginx.core.exception;

/**
 * @Author 翁丞健
 * @Date 2022/5/22 17:22
 * @Version 1.0.0
 */
public class RetryException extends RuntimeException{
    public RetryException(String message) {
        super(message);
    }
}
