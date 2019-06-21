package com.byc.exception;

/**
 * Created by baiyc
 * 2019/5/15/015 15:07
 * Description：登陆异常类
 */
public class LoginException extends RuntimeException {

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }
}
