package com.byc.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by baiyc
 * 2019/5/15/015 19:25
 * Description：
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = AuthorizationException.class)
    public String authorizationExceptionHandler(AuthorizationException e){
        log.error(e.getMessage(),e);
        return "无权限";
    }
}
